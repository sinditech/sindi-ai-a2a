/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse.JSONRPCError;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public class A2AClientJSONRPCError extends A2AClientError {

	private final JSONRPCError error;
	
	/**
	 * @param error
	 */
	public A2AClientJSONRPCError(JSONRPCError error) {
		super((this.error = error).getMessage());
	}
	
	/**
	 * @param error
	 */
	public A2AClientJSONRPCError(JSONRPCErrorResponse error) {
		this(error.getError());
	}

	/**
	 * @return the error
	 */
	public JSONRPCError getError() {
		return error;
	}
}
