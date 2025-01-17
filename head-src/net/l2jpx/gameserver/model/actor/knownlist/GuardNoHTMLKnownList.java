package net.l2jpx.gameserver.model.actor.knownlist;

import org.apache.log4j.Logger;

import net.l2jpx.Config;
import net.l2jpx.gameserver.ai.CtrlIntention;
import net.l2jpx.gameserver.ai.L2CharacterAI;
import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2Object;
import net.l2jpx.gameserver.model.actor.instance.L2GuardNoHTMLInstance;
import net.l2jpx.gameserver.model.actor.instance.L2MonsterInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;

public class GuardNoHTMLKnownList extends AttackableKnownList
{
	private static Logger LOGGER = Logger.getLogger(GuardKnownList.class);
	
	public GuardNoHTMLKnownList(final L2GuardNoHTMLInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public boolean addKnownObject(final L2Object object)
	{
		return addKnownObject(object, null);
	}
	
	@Override
	public boolean addKnownObject(final L2Object object, final L2Character dropper)
	{
		if (!super.addKnownObject(object, dropper))
		{
			return false;
		}
		
		// Set home location of the L2GuardInstance (if not already done)
		if (getActiveChar().getHomeX() == 0)
		{
			getActiveChar().getHomeLocation();
		}
		
		if (object instanceof L2PcInstance)
		{
			// Check if the object added is a L2PcInstance that owns Karma
			L2PcInstance player = (L2PcInstance) object;
			
			if (player.getKarma() > 0)
			{
				if (Config.DEBUG)
				{
					LOGGER.debug(getActiveChar().getObjectId() + ": PK " + player.getObjectId() + " entered scan range");
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
				{
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
			
			player = null;
		}
		else if (Config.ALLOW_GUARDS && object instanceof L2MonsterInstance)
		{
			// Check if the object added is an aggressive L2MonsterInstance
			L2MonsterInstance mob = (L2MonsterInstance) object;
			
			if (mob.isAggressive())
			{
				if (Config.DEBUG)
				{
					LOGGER.debug(getActiveChar().getObjectId() + ": Aggressive mob " + mob.getObjectId() + " entered scan range");
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
				{
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
			
			mob = null;
		}
		
		return true;
	}
	
	@Override
	public boolean removeKnownObject(final L2Object object)
	{
		if (!super.removeKnownObject(object))
		{
			return false;
		}
		
		// Check if the aggroList of the L2GuardInstance is Empty
		if (getActiveChar().noTarget())
		{
			// removeAllKnownObjects();
			// Set the L2GuardInstance to AI_INTENTION_IDLE
			final L2CharacterAI ai = getActiveChar().getAI();
			if (ai != null)
			{
				ai.setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
			}
		}
		
		return true;
	}
	
	@Override
	public final L2GuardNoHTMLInstance getActiveChar()
	{
		return (L2GuardNoHTMLInstance) super.getActiveChar();
	}
}