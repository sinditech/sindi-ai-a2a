/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record TaskPushNotificationConfig(@JsonbProperty String taskId, @JsonbProperty PushNotificationConfig pushNotificationConfig) {

	public TaskPushNotificationConfig {
		taskId = Objects.requireNonNull(taskId, "The task Id is required.");
		pushNotificationConfig = Objects.requireNonNull(pushNotificationConfig, "The push notification configuration for this task is required.");
	}
}
