/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.server.IDGenerator;
import za.co.sindi.ai.a2a.server.IDGeneratorContext;
import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Message.Role;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.TaskArtifactUpdateEvent;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatus;
import za.co.sindi.ai.a2a.types.TaskStatusUpdateEvent;
import za.co.sindi.commons.io.UncheckedException;
import za.co.sindi.commons.utils.Preconditions;

/**
 * Helper class for agents to publish updates to a task's event queue.<p />
 * 
 * Simplifies the process of creating and enqueueing standard task events.
 * 
 * @author Buhake Sindi
 * @since 28 October 2025
 */
public class TaskUpdater {
	private static final Logger LOGGER = Logger.getLogger(TaskUpdater.class.getName());
	
	private static final Set<TaskState> TERMINAL_STATES = Set.of(TaskState.COMPLETED, TaskState.CANCELED, TaskState.FAILED, TaskState.REJECTED);

	private final EventQueue eventQueue;
	private final String taskId;
	private final String contextId;
	private final AtomicBoolean terminalStateReached = new AtomicBoolean(false);
	private final ReentrantLock lock = new ReentrantLock();
	private final IDGenerator artifactIdGenerator;
	private final IDGenerator messageIdGenerator;
	
	/**
	 * Initializes the TaskUpdater.
	 * 
	 * @param eventQueue The {@link EventQueue} associated with the task.
	 * @param taskId The ID of the task.
	 * @param contextId The context ID of the task.
	 */
	public TaskUpdater(EventQueue eventQueue, String taskId, String contextId) {
		this(eventQueue, taskId, contextId, null, null);
	}
	
	/**
	 * Initializes the TaskUpdater.
	 * 
	 * @param eventQueue The {@link EventQueue} associated with the task.
	 * @param taskId The ID of the task.
	 * @param contextId The context ID of the task.
	 * @param artifactIdGenerator ID generator for new artifact IDs. Defaults to UUID generator.
	 * @param messageIdGenerator ID generator for new message IDs. Defaults to UUID generator.
	 */
	public TaskUpdater(EventQueue eventQueue, String taskId, String contextId, IDGenerator artifactIdGenerator,
			IDGenerator messageIdGenerator) {
		super();
		this.eventQueue = Objects.requireNonNull(eventQueue, "An EventQueue is required.");
		this.taskId = Objects.requireNonNull(taskId, "A Task ID is required.");
		this.contextId = Objects.requireNonNull(contextId, "A context ID is required.");
		this.artifactIdGenerator = artifactIdGenerator == null ? IDGenerator.DEFAULT : artifactIdGenerator;
		this.messageIdGenerator = messageIdGenerator == null ? IDGenerator.DEFAULT : artifactIdGenerator;
	}
	
	/**
	 * Updates the status of the task and publishes a {@link TaskStatusUpdateEvent}.
	 * 
	 * @param state The new state of the task.
	 * @param message An optional message associated with the status update.
	 */
	private void updateStatus(final TaskState state, final Message message) {
		updateStatus(state, message, false);
	}
	
	/**
	 * Updates the status of the task and publishes a {@link TaskStatusUpdateEvent}.
	 * 
	 * @param state The new state of the task.
	 * @param message An optional message associated with the status update.
	 * @param isFinal If True, indicates this is the final status update for the task.
	 */
	private void updateStatus(final TaskState state, final Message message, final boolean isFinal) {
		updateStatus(state, message, isFinal, null, null);
	}
	
	/**
	 * Updates the status of the task and publishes a {@link TaskStatusUpdateEvent}.
	 * 
	 * @param state The new state of the task.
	 * @param message An optional message associated with the status update.
	 * @param isFinal If True, indicates this is the final status update for the task.
	 * @param metadata Optional metadata for extensions.
	 * @param timestamp Optional ISO 8601 datetime string. Defaults to current time.
	 */
	private void updateStatus(final TaskState state, final Message message, final boolean isFinal, final Map<String, Object> metadata, final Instant timestamp) {
		lock.lock();
		try {
			Preconditions.checkState(terminalStateReached.get(), "Task " + taskId + " is already in a terminal state.");
			boolean _final = isFinal;
			if (TERMINAL_STATES.contains(state)) {
				_final = true;
				terminalStateReached.set(true);
			}
			
			Instant currentTimestamp = timestamp;
			if (currentTimestamp == null) currentTimestamp = Instant.now();
			eventQueue.enqueueEvent(new TaskStatusUpdateEvent(taskId, contextId, new TaskStatus(state, message, currentTimestamp), _final, metadata));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new UncheckedException(e);
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Adds an artifact chunk to the task and publishes a {@link TaskArtifactUpdateEvent}.
	 * 
	 * @param parts A list of {@link Part} objects forming the artifact chunk.
	 * @param artifactId The ID of the artifact. A new UUID is generated if not provided.
	 * @param name Optional name for the artifact.
	 * @param metadata Optional metadata for the artifact.
	 * @param append Optional boolean indicating if this chunk appends to a previous one.
	 * @param lastChunk Optional boolean indicating if this is the last chunk.
	 * @param extensions Optional list of extensions for the artifact.
	 */
	public void addArtifact(final List<Part> parts, final String artifactId, final String name, final Map<String, Object> metadata, final Boolean append, final Boolean lastChunk, final List<String> extensions) {
		String _artifactId = artifactId;
		if (artifactId == null || artifactId.isEmpty()) _artifactId = artifactIdGenerator.generateId(new IDGeneratorContext(this.taskId, this.contextId));
		try {
			eventQueue.enqueueEvent(new TaskArtifactUpdateEvent(taskId, contextId, new Artifact(_artifactId, null, null, parts == null ? null : parts.toArray(new Part[parts.size()]), metadata, extensions == null ? null : extensions.toArray(new String[extensions.size()])), append, lastChunk, metadata));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new UncheckedException(e);
		}
	}
	
	/**
	 * Marks the task as completed and publishes a final status update.
	 * 
	 * @param message
	 */
	public void complete(final Message message) {
		updateStatus(TaskState.COMPLETED, message, true);
	}
	
	/**
	 * Marks the task as failed and publishes a final status update.
	 * 
	 * @param message
	 */
	public void failed(final Message message) {
		updateStatus(TaskState.FAILED, message, true);
	}
	
	/**
	 * Marks the task as rejected and publishes a final status update.
	 * 
	 * @param message
	 */
	public void reject(final Message message) {
		updateStatus(TaskState.FAILED, message, true);
	}
	
	/**
	 * Marks the task as submitted and publishes a status update.
	 * 
	 * @param message
	 */
	public void submit(final Message message) {
		updateStatus(TaskState.SUBMITTED, message);
	}
	
	/**
	 * "Marks the task as working and publishes a status update.
	 * 
	 * @param message
	 */
	public void startWork(final Message message) {
		updateStatus(TaskState.WORKING, message);
	}
	
	/**
	 * Marks the task as cancelled and publishes a final status update.
	 * 
	 * @param message
	 */
	public void cancel(final Message message) {
		updateStatus(TaskState.CANCELED, message, true);
	}
	
	/**
	 * Marks the task as input required and publishes a status update.
	 * 
	 * @param message
	 */
	public void requiresInput(final Message message) {
		requiresInput(message, false);
	}
	
	/**
	 * Marks the task as input required and publishes a status update.
	 * 
	 * @param message
	 * @param isFinal
	 */
	public void requiresInput(final Message message, final boolean isFinal) {
		updateStatus(TaskState.CANCELED, message, isFinal);
	}
	
	/**
	 * Marks the task as auth required and publishes a status update.
	 * 
	 * @param message
	 */
	public void requiresAuth(final Message message) {
		requiresAuth(message, false);
	}
	
	/**
	 * Marks the task as auth required and publishes a status update.
	 * 
	 * @param message
	 * @param isFinal
	 */
	public void requiresAuth(final Message message, final boolean isFinal) {
		updateStatus(TaskState.AUTH_REQUIRED, message, isFinal);
	}
	
	/**
	 * Creates a new message object sent by the agent for this task/context.
	 * 
	 * <i>Note</i>: This method only <b>creates</b> the message object. It does not
	 *       automatically enqueue it.
	 *               
	 * @param parts A list of {@link Part} objects for the message content.
	 * @param metadata Optional metadata for the message.
	 * @return A new {@link Message} object.
	 */
	public Message newAgentMessage(final List<Part> parts, final Map<String, Object> metadata) {
		return new Message(Role.AGENT, parts == null ? null : parts.toArray(new Part[parts.size()]), metadata, null, null, messageIdGenerator.generateId(new IDGeneratorContext(taskId, contextId)), taskId, contextId);
	}
}
