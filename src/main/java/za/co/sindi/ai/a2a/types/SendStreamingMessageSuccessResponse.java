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
public final class SendStreamingMessageSuccessResponse extends JSONRPCResultResponse<StreamingKind> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public SendStreamingMessageSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id, @JsonbProperty StreamingKind result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public SendStreamingMessageSuccessResponse(RequestId id, StreamingKind result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}
}
