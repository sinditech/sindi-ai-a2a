/**
 * 
 */
package za.co.sindi.ai.a2a.client;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public abstract class A2AClientError extends RuntimeException {

	/**
	 * @param message
	 */
	protected A2AClientError(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	protected A2AClientError(String message, Throwable cause) {
		super(message, cause);
	}
}
