/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

/**
 * Exception raised when attempting to add a queue for a task ID that already exists.
 * 
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class TaskQueueExistsException extends RuntimeException {

	/**
	 * 
	 */
	public TaskQueueExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public TaskQueueExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public TaskQueueExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
