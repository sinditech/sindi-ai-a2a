/**
 * 
 */
package za.co.sindi.ai.a2a.client;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public class A2AClientInvalidStateError extends A2AClientError {

	/**
	 * @param message
	 */
	public A2AClientInvalidStateError(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public A2AClientInvalidStateError(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
}
