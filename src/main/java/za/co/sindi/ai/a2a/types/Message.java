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
 * @since 15 October 2025
 */
public final class Message implements Kind, StreamingKind, Event {
	
	public static final String KIND = "message";
	
	private Role role;
	private Part[] parts;
	private Map<String, Object> metadata;
	private String[] extensions;
	private String[] referenceTaskIds;
	private String messageId;
	private String taskId;
	private String contextId;

	/**
	 * @param role
	 * @param parts
	 * @param messageId
	 */
	public Message(Role role, Part[] parts, String messageId) {
		super();
		this.role = Objects.requireNonNull(role, "Role is required.");
		this.parts = Objects.requireNonNull(parts, "Part(s) is/are required.");
		this.messageId = Objects.requireNonNull(messageId, "Message ID is required.");
	}

	/**
	 * @param role
	 * @param parts
	 * @param metadata
	 * @param extensions
	 * @param referenceTaskIds
	 * @param messageId
	 * @param taskId
	 * @param contextId
	 */
	@JsonbCreator
	public Message(@JsonbProperty("role") Role role, @JsonbProperty("parts") Part[] parts, @JsonbProperty("metadata") Map<String, Object> metadata, @JsonbProperty("extensions") String[] extensions,
			@JsonbProperty("referenceTaskIds") String[] referenceTaskIds, @JsonbProperty("messageId") String messageId, @JsonbProperty("taskId") String taskId, @JsonbProperty("contextId") String contextId) {
		this(role, parts, messageId);
//		this.role = role;
//		this.parts = parts;
		this.metadata = metadata;
		this.extensions = extensions;
		this.referenceTaskIds = referenceTaskIds;
//		this.messageId = messageId;
		this.taskId = taskId;
		this.contextId = contextId;
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
	 * @return the extensions
	 */
	public String[] getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the referenceTaskIds
	 */
	public String[] getReferenceTaskIds() {
		return referenceTaskIds;
	}

	/**
	 * @param referenceTaskIds the referenceTaskIds to set
	 */
	public void setReferenceTaskIds(String[] referenceTaskIds) {
		this.referenceTaskIds = referenceTaskIds;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId the contextId to set
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @return the parts
	 */
	public Part[] getParts() {
		return parts;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	public static enum Role {
		USER("user"),
		AGENT("agent")
		;
		private final String role;

		/**
		 * @param state
		 */
		private Role(String role) {
			this.role = role;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return role;
		}
		
		public static Role of(final String value) {
			for (Role role : values()) {
				if (role.role.equals(value)) return role;
			}
			
			throw new IllegalArgumentException("Invalid role value '" + value + "'.");
		}
	}
}
