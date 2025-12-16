/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse.JSONRPCError;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public final class InvalidRequestError extends JSONRPCError implements A2AError {

	 public final static Integer DEFAULT_CODE = -32600;
	
	/**
	 * @param code
	 * @param message
	 * @param data
	 */
	 @JsonbCreator
	public InvalidRequestError(@JsonbProperty("code") int code, @JsonbProperty("message") String message, @JsonbProperty("data") Map<String, Object> data) {
		super(code, message, data);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalidRequestError(String message) {
		super(DEFAULT_CODE, message);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */
	public InvalidRequestError() {
		this("Request payload validation error.");
		// TODO Auto-generated constructor stub
	}
}
