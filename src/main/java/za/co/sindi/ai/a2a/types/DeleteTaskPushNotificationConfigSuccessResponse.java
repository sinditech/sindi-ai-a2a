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
	 * @param result
	 */
	@JsonbCreator
	public DeleteTaskPushNotificationConfigSuccessResponse(@JsonbProperty JSONRPCVersion jsonrpc, @JsonbProperty RequestId id) {
		super(jsonrpc, id, null);
	}

	/**
	 * @param jsonrpc
	 * @param id
	 * @param result
	 */
	public DeleteTaskPushNotificationConfigSuccessResponse(RequestId id) {
		this(JSONRPCVersion.getLatest(), id);
	}

}
