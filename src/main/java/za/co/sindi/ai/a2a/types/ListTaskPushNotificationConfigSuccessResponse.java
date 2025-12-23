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
public final class ListTaskPushNotificationConfigSuccessResponse extends JSONRPCResultResponse<TaskPushNotificationConfig[]> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public ListTaskPushNotificationConfigSuccessResponse(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("result") TaskPushNotificationConfig[] result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param id
	 * @param result
	 */
	public ListTaskPushNotificationConfigSuccessResponse(RequestId id, TaskPushNotificationConfig[] result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}
}
