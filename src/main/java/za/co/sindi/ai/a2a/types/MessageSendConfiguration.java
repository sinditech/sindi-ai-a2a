/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 233 October 2025
 */
public record MessageSendConfiguration(String[] acceptedOutputModes, Boolean blocking, Integer historyLength, PushNotificationConfig pushNotificationConfig) {

	public MessageSendConfiguration {
		if (historyLength != null && historyLength < 0) throw new IllegalArgumentException("Invalid history length");
	}
	
	@JsonbCreator
	public static MessageSendConfiguration create(@JsonbProperty("acceptedOutputModes") String[] acceptedOutputModes, @JsonbProperty("blocking") Boolean blocking, @JsonbProperty("historyLength") Integer historyLength, @JsonbProperty("pushNotificationConfig") PushNotificationConfig pushNotificationConfig) {
		return new MessageSendConfiguration(acceptedOutputModes, blocking, historyLength, pushNotificationConfig);
	}
}
