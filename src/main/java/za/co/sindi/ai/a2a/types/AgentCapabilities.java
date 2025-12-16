/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public record AgentCapabilities(Boolean streaming, Boolean pushNotifications, Boolean stateTransitionHistory, AgentExtension[] extensions) implements Serializable {
	
	@JsonbCreator
	public static AgentCapabilities create(@JsonbProperty("streaming") Boolean streaming, @JsonbProperty("pushNotifications") Boolean pushNotifications, @JsonbProperty("stateTransitionHistory") Boolean stateTransitionHistory, @JsonbProperty("extensions") AgentExtension[] extensions) {
		return new AgentCapabilities(streaming, pushNotifications, stateTransitionHistory, extensions);
		
	}
}
