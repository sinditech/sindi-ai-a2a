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
public record TaskQueryParams(Integer historyLength, String id, Map<String, Object> metadata) implements Serializable {

	public TaskQueryParams {
		id = Objects.requireNonNull(id, "A unique ID is required.");
		if (historyLength != null && historyLength < 0) throw new IllegalArgumentException("Invalid history length");
	}
	
	public TaskQueryParams(String id) {
		this(null, id, null);
	}
	
	@JsonbCreator
	public static TaskQueryParams create(@JsonbProperty("historyLength") Integer historyLength, @JsonbProperty("id") String id, @JsonbProperty("metadata") Map<String, Object> metadata) {
		return new TaskQueryParams(historyLength, id, metadata);
	}
}
