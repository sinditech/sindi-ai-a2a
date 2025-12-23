/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public final class GetTaskSuccessResponse extends JSONRPCResultResponse<Task> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public GetTaskSuccessResponse(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("result") Task result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param id
	 * @param result
	 */
	public GetTaskSuccessResponse(RequestId id, Task result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}
}
