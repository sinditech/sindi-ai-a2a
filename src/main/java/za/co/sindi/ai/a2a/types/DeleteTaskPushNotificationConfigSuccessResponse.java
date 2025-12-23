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
public final class DeleteTaskPushNotificationConfigSuccessResponse extends JSONRPCResultResponse<Object> {

	/**
	 * @param jsonrpc
	 * @param id
	 */
	@JsonbCreator
	public DeleteTaskPushNotificationConfigSuccessResponse(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id) {
		super(jsonrpc, id, null);
	}

	/**
	 * @param id
	 */
	public DeleteTaskPushNotificationConfigSuccessResponse(RequestId id) {
		this(JSONRPCVersion.getLatest(), id);
	}
}
