package za.co.sindi.ai.a2a.types;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed abstract class JSONRPCResponse implements JSONRPCMessage permits JSONRPCErrorResponse, JSONRPCResultResponse {

	private JSONRPCVersion jsonrpc;
	private RequestId id;
	
	/**
	 * @param jsonrpc
	 * @param id
	 */
	protected JSONRPCResponse(JSONRPCVersion jsonrpc, RequestId id) {
		super();
		this.jsonrpc = Objects.requireNonNull(jsonrpc, "A JSON-RPC protocol version is required.");
		this.id = Objects.requireNonNull(id, "A JSON-RPC request ID is required.");
	}

	@Override
	public JSONRPCVersion getJsonrpc() {
		// TODO Auto-generated method stub
		return jsonrpc;
	}

	@Override
	public RequestId getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
