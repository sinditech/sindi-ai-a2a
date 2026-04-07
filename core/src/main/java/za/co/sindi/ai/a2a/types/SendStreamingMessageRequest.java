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
public final class SendStreamingMessageRequest extends JSONRPCRequest<MessageSendParams> implements A2ARequest {

	public static final String DEFAULT_METHOD = "message/stream";

	/**
	 * @param jsonrpc
	 * @param id
	 * @param params
	 */
	@JsonbCreator
	public SendStreamingMessageRequest(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("params") MessageSendParams params) {
		super(jsonrpc, id, DEFAULT_METHOD, params);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param params
	 */
	public SendStreamingMessageRequest(RequestId id, MessageSendParams params) {
		this(JSONRPCVersion.getLatest(), id, params);
		// TODO Auto-generated constructor stub
	}
}
