/**
 * 
 */
package za.co.sindi.ai.a2a.client;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public class A2AClientHTTPError extends A2AClientError {

	private final int statusCode;
	
	/**
	 * @param statusCode
	 * @param message
	 */
	public A2AClientHTTPError(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	/**
	 * @param statusCode
	 * @param message
	 * @param cause
	 */
	public A2AClientHTTPError(int statusCode, String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
