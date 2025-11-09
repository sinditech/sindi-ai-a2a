/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import za.co.sindi.ai.a2a.types.Task;

/**
 * Interface for sending push notifications for tasks.
 * 
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public interface PushNotificationSender {

	/**
	 * Sends a push notification containing the latest task state.
	 * 
	 * @param task
	 */
	public void sendNotification(final Task task);
}
