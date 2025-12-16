/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record MessageSendParams(MessageSendConfiguration configuration, Message message, Map<String, Object> metadata) implements Serializable {

	public MessageSendParams {
		message = Objects.requireNonNull(message, "A message is required.");
	}
	
	public MessageSendParams(final Message message) {
		this(null, message, null);
	}
	
	@JsonbCreator
	public static MessageSendParams create(@JsonbProperty("configuration") MessageSendConfiguration configuration, @JsonbProperty("message") Message message, @JsonbProperty("metadata") Map<String, Object> metadata) {
		return new MessageSendParams(configuration, message, metadata);
	}
}
