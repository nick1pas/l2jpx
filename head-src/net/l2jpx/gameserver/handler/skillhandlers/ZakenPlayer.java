package net.l2jpx.gameserver.handler.skillhandlers;

import java.util.concurrent.ThreadLocalRandom;

import net.l2jpx.Config;
import net.l2jpx.gameserver.handler.ISkillHandler;
import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2Object;
import net.l2jpx.gameserver.model.L2Skill;
import net.l2jpx.gameserver.model.L2Skill.SkillType;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;

public class ZakenPlayer implements ISkillHandler
{
	// private static Logger LOGGER = Logger.getLogger(ZakenPlayer.class);
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.ZAKENPLAYER
	};
	
	@Override
	public void useSkill(final L2Character activeChar, final L2Skill skill, final L2Object[] targets)
	{
		try
		{
			for (L2Object object : targets)
			{
				if (!(object instanceof L2PcInstance))
				{
					continue;
				}
				
				L2PcInstance target = (L2PcInstance) object;
				int location = ThreadLocalRandom.current().nextInt(1, 13);
				
				if (location == 1)
				{
					target.teleToLocation(55299, 219120, -2952);
				}
				else if (location == 2)
				{
					target.teleToLocation(56363, 218043, -2952);
				}
				else if (location == 3)
				{
					target.teleToLocation(54245, 220162, -2952);
				}
				else if (location == 4)
				{
					target.teleToLocation(56289, 220126, -2952);
				}
				else if (location == 5)
				{
					target.teleToLocation(55299, 219120, -3224);
				}
				else if (location == 6)
				{
					target.teleToLocation(56363, 218043, -3224);
				}
				else if (location == 7)
				{
					target.teleToLocation(54245, 220162, -3224);
				}
				else if (location == 8)
				{
					target.teleToLocation(56289, 220126, -3224);
				}
				else if (location == 9)
				{
					target.teleToLocation(55299, 219120, -3496);
				}
				else if (location == 10)
				{
					target.teleToLocation(56363, 218043, -3496);
				}
				else if (location == 11)
				{
					target.teleToLocation(54245, 220162, -3496);
				}
				else if (location == 12)
				{
					target.teleToLocation(56289, 220126, -3496);
				}
				else
				{
					target.teleToLocation(53930, 217760, -2944);
				}
			}
		}
		catch (final Throwable e)
		{
			if (Config.ENABLE_ALL_EXCEPTIONS)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}