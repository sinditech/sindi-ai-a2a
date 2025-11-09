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
public final class SendMessageSuccessResponse extends JSONRPCResultResponse<Kind> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public SendMessageSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id, @JsonbProperty Kind result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public SendMessageSuccessResponse(RequestId id, Kind result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}
}
