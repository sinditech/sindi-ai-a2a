/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 233 October 2025
 */
public record MessageSendConfiguration(@JsonbProperty String[] acceptedOutputModes, @JsonbProperty Boolean blocking, @JsonbProperty Integer historyLength, @JsonbProperty PushNotificationConfig pushNotificationConfig) {

	public MessageSendConfiguration {
		if (historyLength != null && historyLength < 0) throw new IllegalArgumentException("Invalid history length");
	}
}
