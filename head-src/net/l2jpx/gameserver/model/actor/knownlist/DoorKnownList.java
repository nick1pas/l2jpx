package net.l2jpx.gameserver.model.actor.knownlist;

import net.l2jpx.gameserver.model.L2Object;
import net.l2jpx.gameserver.model.actor.instance.L2DoorInstance;
import net.l2jpx.gameserver.model.actor.instance.L2FortSiegeGuardInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.model.actor.instance.L2SiegeGuardInstance;

public class DoorKnownList extends CharKnownList
{
	public DoorKnownList(final L2DoorInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public final L2DoorInstance getActiveChar()
	{
		return (L2DoorInstance) super.getActiveChar();
	}
	
	@Override
	public int getDistanceToForgetObject(final L2Object object)
	{
		if (object instanceof L2SiegeGuardInstance || object instanceof L2FortSiegeGuardInstance)
		{
			return 800;
		}
		
		if (!(object instanceof L2PcInstance))
		{
			return 0;
		}
		
		return 4000;
	}
	
	@Override
	public int getDistanceToWatchObject(final L2Object object)
	{
		if (object instanceof L2SiegeGuardInstance || object instanceof L2FortSiegeGuardInstance)
		{
			return 600;
		}
		
		if (!(object instanceof L2PcInstance))
		{
			return 0;
		}
		
		return 2000;
	}
}
