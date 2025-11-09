package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed interface JSONRPCMessage extends Serializable permits JSONRPCRequest, JSONRPCResponse {

	public JSONRPCVersion getJsonrpc();
	public RequestId getId();
}
