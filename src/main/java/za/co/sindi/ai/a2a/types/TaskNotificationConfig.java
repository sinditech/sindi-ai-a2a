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
public record TaskNotificationConfig(String taskId, PushNotificationConfig pushNotificationConfig) {

	public TaskNotificationConfig {
		taskId = Objects.requireNonNull(taskId, "The unique identifier (e.g. UUID) of the task is required.");
		pushNotificationConfig = Objects.requireNonNull(pushNotificationConfig, "The push notification configuration for this task is required.");
	}
	
	@JsonbCreator
	public static TaskNotificationConfig create(@JsonbProperty("taskId") String taskId, @JsonbProperty("pushNotificationConfig") PushNotificationConfig pushNotificationConfig) {
		return new TaskNotificationConfig(taskId, pushNotificationConfig);
	}
}
