package net.l2jpx.gameserver.model.actor.instance;

import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.network.serverpackets.ActionFailed;
import net.l2jpx.gameserver.templates.L2NpcTemplate;

public class L2ControlTowerDeadInstance extends L2NpcInstance
{
	// Default ID for this NPC is: 13003
	public L2ControlTowerDeadInstance(final int objectId, final L2NpcTemplate template)
	{
		super(objectId, template);
		setIsInvul(true);
	}
	
	@Override
	public boolean isAttackable()
	{
		return false;
	}
	
	@Override
	public boolean isAutoAttackable(final L2Character attacker)
	{
		return false;
	}
	
	@Override
	public void onForcedAttack(final L2PcInstance player)
	{
		onAction(player);
	}
	
	@Override
	public void onAction(final L2PcInstance player)
	{
		// Do nothing on clic this NPC
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}
