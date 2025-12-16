/**
 * 
 */
package za.co.sindi.ai.a2a.types.rest;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;

/**
 * @author Buhake Sindi
 * @since 18 November 2025
 */
public record CreateTaskPushNotificationConfigRequest(TaskPushNotificationConfig config) {

	public CreateTaskPushNotificationConfigRequest {
		config = Objects.requireNonNull(config, "A TaskPushNotificationConfig is required.");
	}
	
	@JsonbCreator
	public static CreateTaskPushNotificationConfigRequest create(@JsonbProperty("config") TaskPushNotificationConfig config) {
		return new CreateTaskPushNotificationConfigRequest(config);
	}
}
