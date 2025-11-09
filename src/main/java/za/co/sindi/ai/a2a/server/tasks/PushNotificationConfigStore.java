/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.util.List;

import za.co.sindi.ai.a2a.types.PushNotificationConfig;

/**
 * Interface for storing and retrieving push notification configurations for tasks.
 * 
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public interface PushNotificationConfigStore {

	/**
	 * Sets or updates the push notification configuration for a task.
	 * 
	 * @param taskId
	 * @param notificationConfig
	 */
	public void setInfo(final String taskId, final PushNotificationConfig notificationConfig);
	
	/**
	 * Retrieves the push notification configuration for a task.
	 * 
	 * @param taskId
	 * @return
	 */
	public List<PushNotificationConfig> getInfo(final String taskId);
	
	/**
	 * Deletes the push notification configuration for a task.
	 * 
	 * @param taskId
	 */
	default void deleteInfo(final String taskId) {
		deleteInfo(taskId, null);
	}
	
	/**
	 * Deletes the push notification configuration for a task.
	 * 
	 * @param taskId
	 * @param configId
	 */
	public void deleteInfo(final String taskId, final String configId);
}
