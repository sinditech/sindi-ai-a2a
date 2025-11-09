/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public record AgentCapabilities(@JsonbProperty Boolean streaming, @JsonbProperty Boolean pushNotifications, @JsonbProperty Boolean stateTransitionHistory, @JsonbProperty AgentExtension[] extensions) implements Serializable {
	
}
