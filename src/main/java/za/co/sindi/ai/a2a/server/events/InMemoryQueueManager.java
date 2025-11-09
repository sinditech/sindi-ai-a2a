/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import za.co.sindi.commons.io.UncheckedException;

/**
 * This implements the {@link QueueManager} interface using in-memory storage for event
 * queues. It requires all incoming interactions for a given task ID to hit the
 * same binary instance.
 *
 * This implementation is suitable for single-instance deployments but needs
 * a distributed approach for scalable deployments.
 *   
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class InMemoryQueueManager implements QueueManager {
	private static final Logger LOGGER = Logger.getLogger(InMemoryQueueManager.class.getName());
	
	private final ConcurrentMap<String, EventQueue> taskQueue = new ConcurrentHashMap<>();

	@Override
	public void add(String taskId, EventQueue queue) {
		// TODO Auto-generated method stub
		if (taskQueue.containsKey(taskId)) throw new TaskQueueExistsException();
		taskQueue.put(taskId, queue);
	}

	@Override
	public EventQueue get(String taskId) {
		// TODO Auto-generated method stub
		if (taskId == null || !taskQueue.containsKey(taskId)) return null;
		return taskQueue.get(taskId);
	}

	@Override
	public EventQueue tap(String taskId) {
		// TODO Auto-generated method stub
		if (taskId == null || !taskQueue.containsKey(taskId)) return null;
		return taskQueue.get(taskId).tap();
	}

	@Override
	public void close(String taskId) {
		// TODO Auto-generated method stub
		EventQueue queue = taskQueue.remove(taskId);
		if (queue == null) throw new NoTaskQueueException();
		try {
			queue.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new UncheckedException(e);
		}
	}

	@Override
	public EventQueue createOrTap(String taskId) {
		// TODO Auto-generated method stub
		if (!taskQueue.containsKey(taskId)) {
			EventQueue queue = new EventQueue();
			add(taskId, queue);
			return queue;
		}
		
		return tap(taskId);
	}
}
