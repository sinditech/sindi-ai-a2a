/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public record AgentProvider(@JsonbProperty String organization, @JsonbProperty String url) implements Serializable {

	public AgentProvider {
		organization = Objects.requireNonNull(organization, "Organization is required.");
		url = Objects.requireNonNull(url, "URL is required.");
	}
}
