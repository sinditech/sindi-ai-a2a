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
public final class ListTasksSuccessResponse extends JSONRPCResultResponse<ListTasksResult> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public ListTasksSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id, @JsonbProperty ListTasksResult result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public ListTasksSuccessResponse(RequestId id, ListTasksResult result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}

}
