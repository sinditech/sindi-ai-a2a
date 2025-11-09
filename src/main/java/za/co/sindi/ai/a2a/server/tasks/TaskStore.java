/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.types.Task;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public interface TaskStore {

	default void save(final Task task) {
		save(task, null);
	}
	public void save(final Task task, final ServerCallContext context);
	
	default Task load(final String taskId) {
		return load(taskId, null);
	}
	public Task load(final String taskId, final ServerCallContext context);
	
	default void delete(final String taskId) {
		delete(taskId, null);
	}
	public void delete(final String taskId, final ServerCallContext context);
}
