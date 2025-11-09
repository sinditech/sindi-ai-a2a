package za.co.sindi.ai.a2a.types;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 08 February 2025
 */
public sealed abstract class JSONRPCResultResponse<T> extends JSONRPCResponse permits CancelTaskSuccessResponse, SendMessageSuccessResponse, SendStreamingMessageSuccessResponse, SetTaskPushNotificationConfigSuccessResponse, GetTaskPushNotificationConfigSuccessResponse, ListTaskPushNotificationConfigSuccessResponse, DeleteTaskPushNotificationConfigSuccessResponse, GetAuthenticatedExtendedCardSuccessResponse, GetTaskSuccessResponse, ListTasksSuccessResponse {
	
	private T result;

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	protected JSONRPCResultResponse(JSONRPCVersion jsonrpc, RequestId id, T result) {
		super(jsonrpc, id);
		this.result = Objects.requireNonNull(result, "A JSON-RPC result is required.");
	}

	/**
	 * @return the result
	 */
	public T getResult() {
		return result;
	}
}
