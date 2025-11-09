/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

/**
 * Interface for managing the event queue lifecycles per task.
 * 
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public interface QueueManager {

	/**
	 * Adds a new event queue associated with a task ID.
	 * 
	 * @param taskId
	 * @param queue
	 */
	public void add(final String taskId, final EventQueue queue);
	
	/**
	 * Retrieves the event queue for a task ID.
	 * 
	 * @param taskId
	 * @return
	 */
	public EventQueue get(final String taskId);
	
	/**
	 * Creates a child event queue (tap) for an existing task ID.
	 * 
	 * @param taskId
	 * @return A new child {@link EventQueue} instance, or <code>null</code> if the task ID is not found.
	 */
	public EventQueue tap(final String taskId);
	
	/**
	 * Closes and removes the event queue for a task ID.
	 * 
	 * @param taskId
	 */
	public void close(final String taskId);
	
	/**
	 * Creates a queue if one doesn't exist, otherwise taps the existing one.
	 * 
	 * @param taskId
	 * @return
	 * @throws  NoTaskQueueException: If no queue exists for the given `task_id`.
	 */
	public EventQueue createOrTap(final String taskId);
}
