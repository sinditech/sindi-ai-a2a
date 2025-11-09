/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record GetTaskPushNotificationConfigParams(@JsonbProperty String id, @JsonbProperty Map<String, Object> metadata, @JsonbProperty String pushNotificationConfigId) {

	public GetTaskPushNotificationConfigParams {
		id = Objects.requireNonNull(id, "The task Id is required.");
	}
	
	public GetTaskPushNotificationConfigParams(String id) {
		this(id, null, null);
	}
}
