package net.l2jpx.gameserver.taskmanager.tasks;

import org.apache.log4j.Logger;

import net.l2jpx.gameserver.model.entity.sevensigns.SevenSigns;
import net.l2jpx.gameserver.model.entity.sevensigns.SevenSignsFestival;
import net.l2jpx.gameserver.taskmanager.Task;
import net.l2jpx.gameserver.taskmanager.TaskManager;
import net.l2jpx.gameserver.taskmanager.TaskManager.ExecutedTask;
import net.l2jpx.gameserver.taskmanager.TaskTypes;

/**
 * Updates all data for the Seven Signs and Festival of Darkness engines, when time is elapsed.
 * @author Tempy
 */
public class TaskSevenSignsUpdate extends Task
{
	private static final Logger LOGGER = Logger.getLogger(TaskSevenSignsUpdate.class);
	public static final String NAME = "sevensignsupdate";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(final ExecutedTask task)
	{
		try
		{
			SevenSigns.getInstance().saveSevenSignsData(null, true);
			
			if (!SevenSigns.getInstance().isSealValidationPeriod())
			{
				SevenSignsFestival.getInstance().saveFestivalData(false);
			}
			
			LOGGER.info("[GlobalTask] SevenSigns save launched.");
		}
		catch (Exception e)
		{
			LOGGER.error("SevenSigns: Failed to save Seven Signs configuration", e);
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "1800000", "1800000", "");
	}
}