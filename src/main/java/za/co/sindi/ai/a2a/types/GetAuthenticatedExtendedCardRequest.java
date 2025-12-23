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
	 * @param method
	 */
	@JsonbCreator
	public GetAuthenticatedExtendedCardRequest(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("method") String method) {
		super(jsonrpc, id, method, null);
		// TODO Auto-generated constructor stub
		if (!DEFAULT_METHOD.equals(method)) throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + " method.");
	}

	/**
	 * @param id
	 */
	public GetAuthenticatedExtendedCardRequest(RequestId id) {
		this(JSONRPCVersion.getLatest(), id, DEFAULT_METHOD);
		// TODO Auto-generated constructor stub
	}
}
