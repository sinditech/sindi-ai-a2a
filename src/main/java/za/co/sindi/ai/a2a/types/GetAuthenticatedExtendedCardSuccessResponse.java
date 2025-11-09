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
public final class GetAuthenticatedExtendedCardSuccessResponse extends JSONRPCResultResponse<AgentCard> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public GetAuthenticatedExtendedCardSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id, @JsonbProperty AgentCard result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public GetAuthenticatedExtendedCardSuccessResponse(RequestId id, AgentCard result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}

}
