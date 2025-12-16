/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record TaskPushNotificationConfig(String taskId, PushNotificationConfig pushNotificationConfig) {

	public TaskPushNotificationConfig {
		taskId = Objects.requireNonNull(taskId, "The task Id is required.");
		pushNotificationConfig = Objects.requireNonNull(pushNotificationConfig, "The push notification configuration for this task is required.");
	}
	
	@JsonbCreator
	public static TaskPushNotificationConfig create(@JsonbProperty("taskId") String taskId, @JsonbProperty("pushNotificationConfig") PushNotificationConfig pushNotificationConfig) {
		return new TaskPushNotificationConfig(taskId, pushNotificationConfig);
	}
}
