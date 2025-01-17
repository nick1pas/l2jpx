package net.l2jpx.gameserver.taskmanager.tasks;

import org.apache.log4j.Logger;

import net.l2jpx.gameserver.Shutdown;
import net.l2jpx.gameserver.taskmanager.Task;
import net.l2jpx.gameserver.taskmanager.TaskManager.ExecutedTask;

/**
 * @author Layane
 */
public class TaskShutdown extends Task
{
	private static final Logger LOGGER = Logger.getLogger(TaskShutdown.class);
	public static final String NAME = "shutdown";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(final ExecutedTask task)
	{
		LOGGER.info("[GlobalTask] Server Shutdown launched.");
		
		Shutdown handler = new Shutdown(Integer.valueOf(task.getParams()[2]), false, true);
		handler.start();
	}
}