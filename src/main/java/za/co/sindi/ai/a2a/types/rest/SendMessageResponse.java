/**
 * 
 */
package za.co.sindi.ai.a2a.types.rest;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;

/**
 * @author Buhake Sindi
 * @since 03 December 2025
 */
public record SendMessageResponse(Message message, Task task) {

	public SendMessageResponse {
		if (message == null && task == null) {
			throw new IllegalStateException("A response requires a kind (either a message or a task, but not both).");
		}
	}
	
	public SendMessageResponse(final Message message) {
		this(message, null);
	}
	
	public SendMessageResponse(final Task task) {
		this(null, task);
	}
	
	@JsonbCreator
	public static SendMessageResponse create(@JsonbProperty("message") Message message, @JsonbProperty("task") Task task) {
		return new SendMessageResponse(message, task);
	}
}
