	/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public record AgentInterface(String url, TransportProtocol transport) implements Serializable {

	public AgentInterface {
		transport = Objects.requireNonNull(transport, "Transport is required.");
		url = Objects.requireNonNull(url, "URL is required.");
	}
	
	@JsonbCreator
	public static AgentInterface create(@JsonbProperty("url") String url, @JsonbProperty("transport") TransportProtocol transport) {
		 return new AgentInterface(url, transport);
	}
}
