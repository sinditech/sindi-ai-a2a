package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public final class JSONRPCErrorResponse extends JSONRPCResponse {
	
	private JSONRPCError error;

	/**
	 * @param jsonrpc
	 * @param id
	 * @param error
	 */
	@JsonbCreator
	public JSONRPCErrorResponse(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("error") JSONRPCError error) {
		super(jsonrpc, id);
		this.error = Objects.requireNonNull(error, "A JSON-RPC Error is required.");
	}

	/**
	 * @return the error
	 */
	public JSONRPCError getError() {
		return error;
	}

	public static class JSONRPCError implements Serializable {

		private int code;
		private String message;
		private Object data;
		
		@JsonbCreator
		public JSONRPCError(@JsonbProperty int code, @JsonbProperty String message, @JsonbProperty Object data) {
			this(code, message);
			this.data = data;
		}
		
		public JSONRPCError(int code, String message) {
			super();
			this.code = code;
			this.message = Objects.requireNonNull(message, "A string providing a short description of the error is required.");
		}

		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
	}
}
