
package net.l2jpx.gameserver.skills.effects;

import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2Effect;
import net.l2jpx.gameserver.skills.Env;

public class EffectPetrification extends L2Effect
{
	public EffectPetrification(final Env env, final EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return L2Effect.EffectType.PETRIFICATION;
	}
	
	@Override
	public void onStart()
	{
		getEffected().startAbnormalEffect(L2Character.ABNORMAL_EFFECT_HOLD_2);
		// getEffected().setIsParalyzed(true);
		// getEffected().setIsInvul(true);
		getEffected().setPetrified(true);
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopAbnormalEffect(L2Character.ABNORMAL_EFFECT_HOLD_2);
		// getEffected().setIsParalyzed(false);
		// getEffected().setIsInvul(false);
		getEffected().setPetrified(false);
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
