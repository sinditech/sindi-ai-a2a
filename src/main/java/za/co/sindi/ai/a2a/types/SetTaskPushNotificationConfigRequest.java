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
public final class SetTaskPushNotificationConfigRequest extends JSONRPCRequest<TaskPushNotificationConfig> implements A2ARequest {

	public static final String DEFAULT_METHOD = "tasks/pushNotificationConfig/set";

	/**
	 * @param jsonrpc
	 * @param id
	 * @param method
	 * @param params
	 */
	@JsonbCreator
	public SetTaskPushNotificationConfigRequest(@JsonbProperty("jsonrpc") JSONRPCVersion jsonrpc, @JsonbProperty("id") RequestId id, @JsonbProperty("method") String method, @JsonbProperty("params") TaskPushNotificationConfig params) {
		super(jsonrpc, id, method, params);
		// TODO Auto-generated constructor stub
		if (!DEFAULT_METHOD.equals(method)) throw new IllegalArgumentException("Invalid " + this.getClass().getSimpleName() + " method.");
	}

	/**
	 * @param id
	 * @param params
	 */
	public SetTaskPushNotificationConfigRequest(RequestId id, TaskPushNotificationConfig params) {
		this(JSONRPCVersion.getLatest(), id, DEFAULT_METHOD, params);
		// TODO Auto-generated constructor stub
	}
}
