package net.l2jpx.gameserver.skills.l2skills;

import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2Object;
import net.l2jpx.gameserver.model.L2Skill;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.network.SystemMessageId;
import net.l2jpx.gameserver.network.serverpackets.EtcStatusUpdate;
import net.l2jpx.gameserver.network.serverpackets.SystemMessage;
import net.l2jpx.gameserver.skills.effects.EffectCharge;
import net.l2jpx.gameserver.templates.StatsSet;

public class L2SkillCharge extends L2Skill
{
	
	public L2SkillCharge(final StatsSet set)
	{
		super(set);
	}
	
	@Override
	public boolean checkCondition(final L2Character activeChar, final L2Object target, final boolean itemOrWeapon)
	{
		if (activeChar instanceof L2PcInstance)
		{
			final EffectCharge e = (EffectCharge) activeChar.getFirstEffect(this);
			if ((e != null) && (e.numCharges >= getNumCharges()))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_FORCE_HAS_REACHED_MAXIMUM_CAPACITY));
				final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_TO_UNSUITABLE_TERMS);
				sm.addSkillName(getId());
				activeChar.sendPacket(sm);
				return false;
			}
		}
		return super.checkCondition(activeChar, target, itemOrWeapon);
	}
	
	@Override
	public void useSkill(final L2Character caster, final L2Object[] targets)
	{
		if (caster.isAlikeDead())
		{
			return;
		}
		
		// get the effect
		EffectCharge effect = null;
		if (caster instanceof L2PcInstance)
		{
			effect = ((L2PcInstance) caster).getChargeEffect();
		}
		else
		{
			effect = (EffectCharge) caster.getFirstEffect(this);
		}
		
		if (effect != null)
		{
			if (effect.numCharges < getNumCharges())
			{
				effect.numCharges++;
				if (caster instanceof L2PcInstance)
				{
					caster.sendPacket(new EtcStatusUpdate((L2PcInstance) caster));
					final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_FORCE_HAS_INCREASED_TO_S1_LEVEL);
					sm.addNumber(effect.numCharges);
					caster.sendPacket(sm);
				}
			}
			else
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.FORCE_MAXIMUM);
				caster.sendPacket(sm);
			}
			return;
		}
		getEffects(caster, caster, false, false, false);
		
		// effect self :]
		// L2Effect seffect = caster.getEffect(getId());
		// TODO ?? this is always null due to a return in the if block above!
		// if (effect != null && seffect.isSelfEffect())
		// {
		// Replace old effect with new one.
		// seffect.exit();
		// }
		// cast self effect if any
		getEffectsSelf(caster);
	}
	
}
