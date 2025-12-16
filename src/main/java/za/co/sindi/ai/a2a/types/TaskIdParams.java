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
public record TaskIdParams(String id, Map<String, Object> metadata) implements Serializable {

	public TaskIdParams {
		id = Objects.requireNonNull(id, "A unique ID is required.");
	}
	
	public TaskIdParams(String id) {
		this(id, null);
	}
	
	@JsonbCreator
	public static TaskIdParams create(@JsonbProperty("id") String id, @JsonbProperty("metadata") Map<String, Object> metadata) {
		return new TaskIdParams(id, metadata);
	}
}
