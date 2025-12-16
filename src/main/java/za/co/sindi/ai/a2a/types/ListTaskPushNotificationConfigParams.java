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
public record ListTaskPushNotificationConfigParams(String id, Map<String, Object> metadata) {

	public ListTaskPushNotificationConfigParams {
		id = Objects.requireNonNull(id, "The task Id is required.");
	}
	
	public ListTaskPushNotificationConfigParams(String id) {
		this(id, null);
	}
	
	@JsonbCreator
	public static ListTaskPushNotificationConfigParams create(@JsonbProperty("id") String id, @JsonbProperty("metadata") Map<String, Object> metadata) {
		return new ListTaskPushNotificationConfigParams(id, metadata);
	}
}
