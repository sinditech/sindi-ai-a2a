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
public final class TaskArtifactUpdateEvent implements StreamingKind, UpdateEvent {
	
	public static final String KIND = "artifact-update";

	private String taskId;
	private String contextId;
	private Artifact artifact;
	private Boolean append;
	private Boolean lastChunk;
	private Map<String, Object> metadata;
	
	/**
	 * @param taskId
	 * @param contextId
	 * @param artifact
	 * @param append
	 * @param lastChunk
	 * @param metadata
	 */
	@JsonbCreator
	public TaskArtifactUpdateEvent(@JsonbProperty("taskId") String taskId, @JsonbProperty("contextId") String contextId, @JsonbProperty("artifact") Artifact artifact, @JsonbProperty("append") Boolean append,
			@JsonbProperty("lastChunk") Boolean lastChunk, @JsonbProperty("metadata") Map<String, Object> metadata) {
		super();
		this.taskId = taskId;
		this.contextId = contextId;
		this.artifact = artifact;
		this.append = append;
		this.lastChunk = lastChunk;
		this.metadata = metadata;
	}

	/**
	 * @param taskId
	 * @param contextId
	 * @param artifact
	 */
	public TaskArtifactUpdateEvent(String taskId, String contextId, Artifact artifact) {
		super();
		this.taskId = Objects.requireNonNull(taskId, "Task Id is required");
		this.contextId = Objects.requireNonNull(contextId, "Context Id is reuired.");
		this.artifact = Objects.requireNonNull(artifact, "Artifact is required.");
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
	 * @return the artifact
	 */
	public Artifact getArtifact() {
		return artifact;
	}

	/**
	 * @return the append
	 */
	public Boolean getAppend() {
		return append;
	}

	/**
	 * @return the lastChunk
	 */
	public Boolean getLastChunk() {
		return lastChunk;
	}
}
