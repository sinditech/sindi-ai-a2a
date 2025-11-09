/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.Collections;
import java.util.List;

import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.Task.Builder;

/**
 * @author Buhake Sindi
 * @since 30 October 2025
 */
public class Tasks {

	private Tasks() {
		throw new AssertionError("Private constructor.");
	}
	
	/**
	 * Applies history_length parameter on task and returns a new task object.
	 * 
	 * @param task The original task object with complete history
	 * @param historyLength History length configuration value
	 * @return  A new task object with limited history
	 */
	public static Task applyHistoryLength(final Task task, final Integer historyLength) {
		
		if (historyLength != null && task.getHistory() != null && task.getHistory().length > 0) {
			List<Message> histories = List.of(task.getHistory());
			List<Message> limitedHistory =  (historyLength > 0) ? histories.subList(task.getHistory().length - historyLength, task.getHistory().length) : Collections.emptyList();
			
			return new Builder(task).history(limitedHistory.toArray(new Message[limitedHistory.size()])).build();
		}
		
		return task;
	}
}
