/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

/**
 * Exception raised when attempting to access or close a queue for a task ID that does not exist.
 * 
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public class EventQueueClosedException extends RuntimeException {

	/**
	 * 
	 */
	public EventQueueClosedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EventQueueClosedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EventQueueClosedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
