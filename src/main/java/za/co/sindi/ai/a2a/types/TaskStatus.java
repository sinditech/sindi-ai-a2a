/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 15 October 2025
 */
public record TaskStatus(TaskState state, Message message, Instant timestamp) implements Serializable {

	public TaskStatus {
		state = Objects.requireNonNull(state, "Task state is required.");
	}
	
	public TaskStatus(TaskState state) {
        this(state, null, null);
    }
	
	@JsonbCreator
	public static TaskStatus create(@JsonbProperty("state") TaskState state, @JsonbProperty("message") Message message, @JsonbProperty("timestamp") /*OffsetDateTime*/ Instant timestamp) {
		return new TaskStatus(state, message, timestamp);
	}
}
