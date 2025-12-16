package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbTransient;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public non-sealed abstract class JSONRPCRequest<T> implements JSONRPCMessage {
	
	private JSONRPCVersion jsonrpc;
	private RequestId id;
	@JsonbTransient
	private String requestMethod; //Renamed from "method" to "requestMethod" because of JSON-B serialization.
//	private Map<String, Object> params;
	private T params;

	/**
	 * @param jsonrpc
	 * @param method
	 */
	protected JSONRPCRequest(JSONRPCVersion jsonrpc, String method) {
		super();
		this.jsonrpc = Objects.requireNonNull(jsonrpc, "A JSON-RPC protocol version is required.");
//		if (this.jsonrpc != JSONRPCVersion.VERSION_2_0) {
//			throw new IllegalArgumentException("Invalid JSON-RPC protocol version");
//		}
		this.requestMethod = Objects.requireNonNull(method, "A JSON-RPC method is required.");
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param method
	 * @param params
	 */
	protected JSONRPCRequest(JSONRPCVersion jsonrpc, RequestId id, String method, T params) {
		this(jsonrpc, method);
//		this.jsonrpc = jsonrpc;
		this.id = id;
//		this.method = method;
		this.params = params;
	}

	/**
	 * @return the id
	 */
	public RequestId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(RequestId id) {
		this.id = id;
	}

	/**
	 * @return the params
	 */
	public T getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(T params) {
		this.params = params;
	}

	/**
	 * @return the jsonrpc
	 */
	public JSONRPCVersion getJsonrpc() {
		return jsonrpc;
	}

	/**
	 * @return the method
	 */
	public String getRequestMethod() {
		return requestMethod;
	}	
}
