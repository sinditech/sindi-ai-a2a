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
public record DeleteTaskPushNotificationConfigParams(String id, Map<String, Object> metadata, String pushNotificationConfigId) {

	public DeleteTaskPushNotificationConfigParams {
		id = Objects.requireNonNull(id, "The task Id is required.");
	}
	
	public DeleteTaskPushNotificationConfigParams(String id) {
		this(id, null, null);
	}
	
	public DeleteTaskPushNotificationConfigParams(String id, String pushNotificationConfigId) {
		this(id, null, pushNotificationConfigId);
	}
	
	@JsonbCreator
	public static DeleteTaskPushNotificationConfigParams create(@JsonbProperty("id") String id, @JsonbProperty("metadata") Map<String, Object> metadata, @JsonbProperty("pushNotificationConfigId") String pushNotificationConfigId) {
		return new DeleteTaskPushNotificationConfigParams(id, metadata, pushNotificationConfigId);
	}
}
