/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public final class GetAuthenticatedExtendedCardRequest extends JSONRPCRequest<Object> implements A2ARequest {

	public static final String DEFAULT_METHOD = "agent/getAuthenticatedExtendedCard";

	/**
	 * @param jsonrpc
	 * @param id
	 */
	@JsonbCreator
	public GetAuthenticatedExtendedCardRequest(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id) {
		super(jsonrpc, id, DEFAULT_METHOD, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 */
	public GetAuthenticatedExtendedCardRequest(RequestId id) {
		this(JSONRPCVersion.getLatest(), id);
		// TODO Auto-generated constructor stub
	}
}
