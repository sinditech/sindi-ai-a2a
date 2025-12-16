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
public record AgentProvider(String organization, String url) implements Serializable {

	public AgentProvider {
		organization = Objects.requireNonNull(organization, "Organization is required.");
		url = Objects.requireNonNull(url, "URL is required.");
	}
	
	@JsonbCreator
	public static AgentProvider create(@JsonbProperty("organization") String organization, @JsonbProperty("url") String url) {
		return new AgentProvider(organization, url);
	}
}
