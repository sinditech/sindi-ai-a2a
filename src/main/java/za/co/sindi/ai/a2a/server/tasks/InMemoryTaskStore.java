/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.types.Task;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public class InMemoryTaskStore implements TaskStore {
	
	private final ConcurrentMap<String, Task> store = new ConcurrentHashMap<>();

	@Override
	public void save(Task task, ServerCallContext context) {
		// TODO Auto-generated method stub
		store.put(task.getId(), task);
	}

	@Override
	public Task load(String taskId, ServerCallContext context) {
		// TODO Auto-generated method stub
		return store.get(taskId);
	}

	@Override
	public void delete(String taskId, ServerCallContext context) {
		// TODO Auto-generated method stub
		store.remove(taskId);
	}
}
