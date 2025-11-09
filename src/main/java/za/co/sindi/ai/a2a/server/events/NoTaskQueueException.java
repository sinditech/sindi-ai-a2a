/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

/**
 * Exception raised when attempting to access or close a queue for a task ID that does not exist.
 * 
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class NoTaskQueueException extends RuntimeException {

	/**
	 * 
	 */
	public NoTaskQueueException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoTaskQueueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoTaskQueueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
