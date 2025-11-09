/**
 * 
 */
package za.co.sindi.ai.a2a.server;

import java.util.Objects;

import za.co.sindi.ai.a2a.types.A2AError;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse.JSONRPCError;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class A2AServerError extends RuntimeException {

	private final A2AError error;

	/**
	 * @param error
	 */
	public A2AServerError(A2AError error) {
		super();
		this.error = Objects.requireNonNull(error, "An A2A error is required.");
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return ((JSONRPCError)error).getMessage();
	}
}
