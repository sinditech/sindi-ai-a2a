/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 15 October 2025
 */
public final class Task implements Kind, StreamingKind, Event {

	public static final String KIND = "task";
	
	private String id;
	private String contextId;
	private TaskStatus status;
	private Message[] history;
	private Artifact[] artifacts;
	private Map<String, Object> metadata;
	
	/**
	 * @param id
	 * @param contextId
	 * @param status
	 * @param history
	 * @param artifacts
	 * @param metadata
	 */
	@JsonbCreator
	public Task(@JsonbProperty("id") String id, @JsonbProperty("contextId") String contextId, @JsonbProperty("status") TaskStatus status, @JsonbProperty("history") Message[] history, @JsonbProperty("artifacts") Artifact[] artifacts,
			@JsonbProperty("metadata") Map<String, Object> metadata) {
		super();
		this.id = id;
		this.contextId = contextId;
		this.status = status;
		this.history = history;
		this.artifacts = artifacts;
		this.metadata = metadata;
	}

	/**
	 * @param id
	 * @param contextId
	 * @param status
	 */
	public Task(String id, String contextId, TaskStatus status) {
		super();
		this.id = Objects.requireNonNull(id, "Task Id is required.");
		this.contextId = Objects.requireNonNull(contextId, "Context Id is required.");
		this.status = Objects.requireNonNull(status, "Task status is required.");
	}

	/**
	 * @return the history
	 */
	public Message[] getHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(Message[] history) {
		this.history = history;
	}

	/**
	 * @return the artifacts
	 */
	public Artifact[] getArtifacts() {
		return artifacts;
	}

	/**
	 * @param artifacts the artifacts to set
	 */
	public void setArtifacts(Artifact[] artifacts) {
		this.artifacts = artifacts;
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
	 * @return the id
	 */
	public String getId() {
		return id;
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
	
	public static final class Builder {
		private String id;
		private String contextId;
		private TaskStatus status;
		private Message[] history;
		private Artifact[] artifacts;
		private Map<String, Object> metadata;
		
		public Builder() {}
		
		public Builder(final Task task) {
			id(task.getId()).
			contextId(task.getContextId()).
			status(task.getStatus()).
			artifacts(task.getArtifacts()).
			history(task.getHistory()).
			metadata(task.getMetadata());
		}
		
		/**
		 * @param id the id to set
		 */
		public Builder id(String id) {
			this.id = id;
			return this;
		}

		/**
		 * @param contextId the contextId to set
		 */
		public Builder contextId(String contextId) {
			this.contextId = contextId;
			return this;
		}

		/**
		 * @param status the status to set
		 */
		public Builder status(TaskStatus status) {
			this.status = status;
			return this;
		}

		/**
		 * @param history the history to set
		 */
		public Builder history(Message[] history) {
			this.history = history;
			return this;
		}
		
		/**
		 * @param history the history to set
		 */
		public Builder history(List<Message> history) {
			return history(history.toArray(new Message[history.size()]));
		}

		/**
		 * @param artifacts the artifacts to set
		 */
		public Builder artifacts(Artifact[] artifacts) {
			this.artifacts = artifacts;
			return this;
		}

		/**
		 * @param metadata the metadata to set
		 */
		public Builder metadata(Map<String, Object> metadata) {
			this.metadata = metadata;
			return this;
		}

		public Task build() {
			return new Task(id, contextId, status, history, artifacts, metadata);
		}
	}
}
