/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record GetTaskPushNotificationConfigParams(String id, Map<String, Object> metadata, String pushNotificationConfigId) {

	public GetTaskPushNotificationConfigParams {
		id = Objects.requireNonNull(id, "The task Id is required.");
	}
	
	public GetTaskPushNotificationConfigParams(String id) {
		this(id, null, null);
	}
	
	public GetTaskPushNotificationConfigParams(String id, String pushNotificationConfigId) {
		this(id, null, pushNotificationConfigId);
	}
	
	@JsonbCreator
	public static GetTaskPushNotificationConfigParams create(@JsonbProperty("id") String id, @JsonbProperty("metadata") Map<String, Object> metadata, @JsonbProperty("pushNotificationId") String pushNotificationConfigId) {
		return new GetTaskPushNotificationConfigParams(id, metadata, pushNotificationConfigId);
	}
}
