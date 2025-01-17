
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

public class L2SkillChargeEffect extends L2Skill
{
	final int chargeSkillId;
	
	public L2SkillChargeEffect(final StatsSet set)
	{
		super(set);
		chargeSkillId = set.getInteger("charge_skill_id");
	}
	
	@Override
	public boolean checkCondition(final L2Character activeChar, final L2Object target, final boolean itemOrWeapon)
	{
		if (activeChar instanceof L2PcInstance)
		{
			final L2PcInstance player = (L2PcInstance) activeChar;
			final EffectCharge e = (EffectCharge) player.getFirstEffect(chargeSkillId);
			if (e == null || e.numCharges < getNumCharges())
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_TO_UNSUITABLE_TERMS);
				sm.addSkillName(getId());
				activeChar.sendPacket(sm);
				return false;
			}
		}
		return super.checkCondition(activeChar, target, itemOrWeapon);
	}
	
	@Override
	public void useSkill(final L2Character activeChar, final L2Object[] targets)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		// get the effect
		final EffectCharge effect = (EffectCharge) activeChar.getFirstEffect(chargeSkillId);
		if (effect == null || effect.numCharges < getNumCharges())
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_TO_UNSUITABLE_TERMS);
			sm.addSkillName(getId());
			activeChar.sendPacket(sm);
			return;
		}
		
		// decrease?
		effect.numCharges -= getNumCharges();
		
		// update icons
		// activeChar.updateEffectIcons();
		
		// maybe exit? no charge
		if (effect.numCharges == 0)
		{
			effect.exit(false);
		}
		
		// apply effects
		if (hasEffects())
		{
			for (final L2Object target : targets)
			{
				getEffects(activeChar, (L2Character) target, false, false, false);
			}
		}
		if (activeChar instanceof L2PcInstance)
		{
			activeChar.sendPacket(new EtcStatusUpdate((L2PcInstance) activeChar));
		}
	}
}
