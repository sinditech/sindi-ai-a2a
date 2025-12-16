/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public final class TaskStatusUpdateEvent implements StreamingKind, UpdateEvent {
	
	public static final String KIND = "status-update";

	private String taskId;
	private String contextId;
	private TaskStatus status;
	private boolean isFinal;
	private Map<String, Object> metadata;
	
	/**
	 * @param taskId
	 * @param contextId
	 * @param status
	 * @param isFinal
	 * @param metadata
	 */
	@JsonbCreator
	public TaskStatusUpdateEvent(@JsonbProperty("taskId") String taskId, @JsonbProperty("contextId") String contextId, @JsonbProperty("status") TaskStatus status, @JsonbProperty("final") boolean isFinal,
			@JsonbProperty("metadata") Map<String, Object> metadata) {
		this(taskId, contextId, status, isFinal);
		this.metadata = metadata;
	}

	/**
	 * @param taskId
	 * @param contextId
	 * @param status
	 * @param isFinal
	 */
	public TaskStatusUpdateEvent(String taskId, String contextId, TaskStatus status, boolean isFinal) {
		super();
		this.taskId = Objects.requireNonNull(taskId, "Task Id is required");
		this.contextId = Objects.requireNonNull(contextId, "Context Id is reuired.");
		this.status = Objects.requireNonNull(status, "Task status is required.");
		this.isFinal = isFinal;
	}

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * @return the isFinal
	 */
	public boolean isFinal() {
		return isFinal;
	}
}
