/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record MessageSendParams(@JsonbProperty MessageSendConfiguration configuration, @JsonbProperty Message message, @JsonbProperty Map<String, Object> metadata) implements Serializable {

	public MessageSendParams {
		message = Objects.requireNonNull(message, "A message is required.");
	}
	
	public MessageSendParams(final Message message) {
		this(null, message, null);
	}
}
