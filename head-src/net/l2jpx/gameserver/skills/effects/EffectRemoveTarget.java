package net.l2jpx.gameserver.skills.effects;

import net.l2jpx.gameserver.ai.CtrlIntention;
import net.l2jpx.gameserver.model.L2Effect;
import net.l2jpx.gameserver.model.actor.instance.L2GrandBossInstance;
import net.l2jpx.gameserver.model.actor.instance.L2RaidBossInstance;
import net.l2jpx.gameserver.skills.Env;

/**
 * @author programmos, L2JFrozen
 */
public class EffectRemoveTarget extends L2Effect
{
	public EffectRemoveTarget(final Env env, final EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.REMOVE_TARGET;
	}
	
	@Override
	public boolean onActionTime()
	{
		// nothing
		return false;
	}
	
	/**
	 * @see net.l2jpx.gameserver.model.L2Effect#onExit()
	 */
	
	@Override
	public void onExit()
	{
		try
		{
			// nothing
			super.onExit();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @see net.l2jpx.gameserver.model.L2Effect#onStart()
	 */
	@Override
	public void onStart()
	{
		// RaidBoss and GrandBoss are immune to RemoveTarget effect
		if (getEffected() instanceof L2RaidBossInstance || getEffected() instanceof L2GrandBossInstance)
		{
			return;
		}
		
		try
		{
			getEffected().setTarget(null);
			getEffected().abortAttack();
			getEffected().abortCast();
			getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, getEffector());
			super.onStart();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}