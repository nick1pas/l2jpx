package net.l2jpx.gameserver.model.actor.instance;

import net.l2jpx.gameserver.ai.CtrlEvent;
import net.l2jpx.gameserver.datatables.sql.SpawnTable;
import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.spawn.L2Spawn;
import net.l2jpx.gameserver.network.clientpackets.Say2;
import net.l2jpx.gameserver.network.serverpackets.CreatureSay;
import net.l2jpx.gameserver.templates.L2NpcTemplate;
import net.l2jpx.util.random.Rnd;

public class L2PenaltyMonsterInstance extends L2MonsterInstance
{
	private L2PcInstance ptk;
	
	public L2PenaltyMonsterInstance(final int objectId, final L2NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public L2Character getMostHated()
	{
		return ptk; // zawsze attakuje tylko 1 osobe chodzby nie wiem co xD
	}
	
	@Deprecated
	public void notifyPlayerDead()
	{
		// Monster kill player and can by deleted
		deleteMe();
		
		L2Spawn spawn = getSpawn();
		if (spawn != null)
		{
			spawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(spawn, false);
			spawn = null;
		}
	}
	
	public void setPlayerToKill(final L2PcInstance ptk)
	{
		if (Rnd.nextInt(100) <= 80)
		{
			CreatureSay cs = new CreatureSay(getObjectId(), Say2.ALL, getName(), "mmm your bait was delicious");
			this.broadcastPacket(cs);
			cs = null;
		}
		this.ptk = ptk;
		addDamageHate(ptk, 10, 10);
		getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, ptk);
		addAttackerToAttackByList(ptk);
	}
	
	@Override
	public boolean doDie(final L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		if (Rnd.nextInt(100) <= 75)
		{
			CreatureSay cs = new CreatureSay(getObjectId(), Say2.ALL, getName(), "I will tell fishes not to take your bait");
			this.broadcastPacket(cs);
			cs = null;
		}
		return true;
	}
	
}
