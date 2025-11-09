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
public final class GetTaskPushNotificationConfigSuccessResponse extends JSONRPCResultResponse<TaskPushNotificationConfig> {

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	@JsonbCreator
	public GetTaskPushNotificationConfigSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id, @JsonbProperty TaskPushNotificationConfig result) {
		super(jsonrpc, id, result);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public GetTaskPushNotificationConfigSuccessResponse(RequestId id, TaskPushNotificationConfig result) {
		this(JSONRPCVersion.getLatest(), id, result);
	}

}
