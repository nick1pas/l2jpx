package net.l2jpx.gameserver.model.actor.stat;

import net.l2jpx.gameserver.datatables.sql.L2PetDataTable;
import net.l2jpx.gameserver.datatables.xml.ExperienceData;
import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2Skill;
import net.l2jpx.gameserver.model.actor.instance.L2PetInstance;
import net.l2jpx.gameserver.network.SystemMessageId;
import net.l2jpx.gameserver.network.serverpackets.PetInfo;
import net.l2jpx.gameserver.network.serverpackets.StatusUpdate;
import net.l2jpx.gameserver.network.serverpackets.SystemMessage;
import net.l2jpx.gameserver.skills.Stats;

public class PetStat extends SummonStat
{
	public PetStat(final L2PetInstance activeChar)
	{
		super(activeChar);
	}
	
	public boolean addExp(final int value)
	{
		if (!super.addExp(value))
		{
			return false;
		}
		
		/*
		 * Micht : Use of PetInfo for C5 StatusUpdate su = new StatusUpdate(getActiveChar().getObjectId()); su.addAttribute(StatusUpdate.EXP, getExp()); getActiveChar().broadcastPacket(su);
		 */
		getActiveChar().broadcastPacket(new PetInfo(getActiveChar()));
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		getActiveChar().updateEffectIcons(true);
		
		return true;
	}
	
	@Override
	public boolean addExpAndSp(final long addToExp, final int addToSp)
	{
		if (!super.addExpAndSp(addToExp, addToSp))
		{
			return false;
		}
		
		SystemMessage sm = new SystemMessage(SystemMessageId.PET_EARNED_S1_EXP);
		sm.addNumber((int) addToExp);
		
		getActiveChar().getOwner().sendPacket(sm);
		sm = null;
		
		return true;
	}
	
	@Override
	public final boolean addLevel(final byte value)
	{
		if (getLevel() + value > ExperienceData.getInstance().getMaxLevel() - 1)
		{
			return false;
		}
		
		final boolean levelIncreased = super.addLevel(value);
		
		// Sync up exp with current level
		if (getExp() > getExpForLevel(getLevel() + 1) || getExp() < getExpForLevel(getLevel()))
		{
			setExp(ExperienceData.getInstance().getExpForLevel(getLevel()));
		}
		
		if (levelIncreased)
		{
			getActiveChar().getOwner().sendMessage("Your pet has increased it's level.");
		}
		
		StatusUpdate su = new StatusUpdate(getActiveChar().getObjectId());
		su.addAttribute(StatusUpdate.LEVEL, getLevel());
		su.addAttribute(StatusUpdate.MAX_HP, getMaxHp());
		su.addAttribute(StatusUpdate.MAX_MP, getMaxMp());
		getActiveChar().broadcastPacket(su);
		su = null;
		
		// Send a Server->Client packet PetInfo to the L2PcInstance
		getActiveChar().getOwner().sendPacket(new PetInfo(getActiveChar()));
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		getActiveChar().updateEffectIcons(true);
		
		if (getActiveChar().getControlItem() != null)
		{
			getActiveChar().getControlItem().setEnchantLevel(getLevel());
		}
		
		return levelIncreased;
	}
	
	@Override
	public final long getExpForLevel(final int level)
	{
		return L2PetDataTable.getInstance().getPetData(getActiveChar().getNpcId(), level).getPetMaxExp();
	}
	
	@Override
	public L2PetInstance getActiveChar()
	{
		return (L2PetInstance) super.getActiveChar();
	}
	
	public final int getFeedBattle()
	{
		return getActiveChar().getPetData().getPetFeedBattle();
	}
	
	public final int getFeedNormal()
	{
		return getActiveChar().getPetData().getPetFeedNormal();
	}
	
	@Override
	public void setLevel(final int value)
	{
		getActiveChar().stopFeed();
		super.setLevel(value);
		
		getActiveChar().setPetData(L2PetDataTable.getInstance().getPetData(getActiveChar().getTemplate().npcId, getLevel()));
		getActiveChar().startFeed(false);
		
		if (getActiveChar().getControlItem() != null)
		{
			getActiveChar().getControlItem().setEnchantLevel(getLevel());
		}
	}
	
	public final int getMaxFeed()
	{
		return getActiveChar().getPetData().getPetMaxFeed();
	}
	
	@Override
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, getActiveChar().getPetData().getPetMaxHP(), null, null);
	}
	
	@Override
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, getActiveChar().getPetData().getPetMaxMP(), null, null);
	}
	
	@Override
	public int getMAtk(final L2Character target, final L2Skill skill)
	{
		double attack = getActiveChar().getPetData().getPetMAtk();
		Stats stat = skill == null ? null : skill.getStat();
		
		if (stat != null)
		{
			switch (stat)
			{
				case AGGRESSION:
					attack += getActiveChar().getTemplate().baseAggression;
					break;
				case BLEED:
					attack += getActiveChar().getTemplate().baseBleed;
					break;
				case POISON:
					attack += getActiveChar().getTemplate().basePoison;
					break;
				case STUN:
					attack += getActiveChar().getTemplate().baseStun;
					break;
				case ROOT:
					attack += getActiveChar().getTemplate().baseRoot;
					break;
				case MOVEMENT:
					attack += getActiveChar().getTemplate().baseMovement;
					break;
				case CONFUSION:
					attack += getActiveChar().getTemplate().baseConfusion;
					break;
				case SLEEP:
					attack += getActiveChar().getTemplate().baseSleep;
					break;
				case FIRE:
					attack += getActiveChar().getTemplate().baseFire;
					break;
				case WIND:
					attack += getActiveChar().getTemplate().baseWind;
					break;
				case WATER:
					attack += getActiveChar().getTemplate().baseWater;
					break;
				case EARTH:
					attack += getActiveChar().getTemplate().baseEarth;
					break;
				case HOLY:
					attack += getActiveChar().getTemplate().baseHoly;
					break;
				case DARK:
					attack += getActiveChar().getTemplate().baseDark;
					break;
			}
		}
		
		if (skill != null)
		{
			attack += skill.getPower();
		}
		
		stat = null;
		
		return (int) calcStat(Stats.MAGIC_ATTACK, attack, target, skill);
	}
	
	@Override
	public int getMDef(final L2Character target, final L2Skill skill)
	{
		final double defence = getActiveChar().getPetData().getPetMDef();
		
		return (int) calcStat(Stats.MAGIC_DEFENCE, defence, target, skill);
	}
	
	@Override
	public int getPAtk(final L2Character target)
	{
		return (int) calcStat(Stats.POWER_ATTACK, getActiveChar().getPetData().getPetPAtk(), target, null);
	}
	
	@Override
	public int getPDef(final L2Character target)
	{
		return (int) calcStat(Stats.POWER_DEFENCE, getActiveChar().getPetData().getPetPDef(), target, null);
	}
	
	@Override
	public int getAccuracy()
	{
		return (int) calcStat(Stats.ACCURACY_COMBAT, getActiveChar().getPetData().getPetAccuracy(), null, null);
	}
	
	@Override
	public int getCriticalHit(final L2Character target, final L2Skill skill)
	{
		return (int) calcStat(Stats.CRITICAL_RATE, getActiveChar().getPetData().getPetCritical(), target, null);
	}
	
	@Override
	public int getEvasionRate(final L2Character target)
	{
		return (int) calcStat(Stats.EVASION_RATE, getActiveChar().getPetData().getPetEvasion(), target, null);
	}
	
	@Override
	public int getRunSpeed()
	{
		return (int) calcStat(Stats.RUN_SPEED, getActiveChar().getPetData().getPetSpeed(), null, null);
	}
	
	@Override
	public int getPAtkSpd()
	{
		return (int) calcStat(Stats.POWER_ATTACK_SPEED, getActiveChar().getPetData().getPetAtkSpeed(), null, null);
	}
	
	@Override
	public int getMAtkSpd()
	{
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, getActiveChar().getPetData().getPetCastSpeed(), null, null);
	}
}
