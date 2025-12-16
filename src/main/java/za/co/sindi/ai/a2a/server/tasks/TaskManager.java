/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.InvalidParamsError;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskArtifactUpdateEvent;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatus;
import za.co.sindi.ai.a2a.types.TaskStatusUpdateEvent;
import za.co.sindi.ai.a2a.utils.Helpers;

/**
 * Helps manage a task's lifecycle during execution of a request.
 * 
 * Responsible for retrieving, saving, and updating the `Task` object based on
 * events received from the agent.
 * 
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public class TaskManager {
	private static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());
	
	private String taskId;
	private String contextId;
	private final TaskStore taskStore;
	private Message initialMessage;
	private ServerCallContext context;
	private volatile Task currentTask;
	
	/**
	 * Initializes the TaskManager.
	 * 
	 * @param taskStore The {@link TaskStore} instance for persistence.
	 * @throws NullPointerException if <code>taskStore</code> is <code>null</code>.
	 */
	public TaskManager(TaskStore taskStore) {
		super();
		this.taskStore = Objects.requireNonNull(taskStore, "A task store is required.");
	}

	/**
	 * Initializes the TaskManager.
	 * 
	 * @param taskId The ID of the task, if known from the request.
	 * @param contextId The ID of the context, if known from the request.
	 * @param taskStore The {@link TaskStore} instance for persistence.
	 * @param initialMessage The {@link Message} that initiated the task, if any. Used when creating a new task object.
	 * @param context  The {@link ServerCallContext} that this task is produced under.
	 * @throws NullPointerException if <code>taskStore</code> is <code>null</code>.
	 */
	public TaskManager(String taskId, String contextId, TaskStore taskStore, Message initialMessage,
			ServerCallContext context) {
		this(taskStore);
		if (taskId != null && taskId.isEmpty()) {
			throw new IllegalArgumentException("Task ID must be a non-empty string.");
		}
		this.taskId = taskId;
		this.contextId = contextId;
		this.initialMessage = initialMessage;
		this.context = context;
		LOGGER.fine(String.format("TaskManager initialized with taskId: %s, contextId: %s", taskId, contextId));
	}
	
	/**
	 * Retrieves the current task object, either from memory or the store.
	 * 
	 * If <code>taskId</code> is set, it first checks the in-memory `_current_task`,
	 * then attempts to load it from the <code>taskStore</code>.
	 * 
	 * @return The {@link Task} object if found, otherwise <code>null</code>.
	 */
	public Task getTask() {
		if (taskId == null) {
			LOGGER.fine("taskId is not set, cannot get task.");
			return null;
		}
		
		if (currentTask != null) return currentTask;
		
		LOGGER.fine(String.format("Attempting to get task from store with id: %s", taskId));
		currentTask = taskStore.load(taskId, context);
		
		if (currentTask == null) {
			LOGGER.fine(String.format("Task %s not found.", taskId));
		} else {
			LOGGER.fine(String.format("Task %s retrieved successfully.", taskId));
		}
		
		return currentTask;
	}
	
	private void matchAndUpdateTaskAndContextIds(final String eventTaskId, final String eventContextId) {
		if (taskId != null && !taskId.equals(eventTaskId)) {
			throw new A2AServerError(new InvalidParamsError("Task in event doesn't match TaskManager " + taskId + " : " + eventTaskId));
		}
		if (taskId == null) taskId = eventTaskId;

		if (contextId != null && !contextId.equals(eventContextId)) {
			throw new A2AServerError(new InvalidParamsError("Context in event doesn't match TaskManager " + contextId + " : " + eventContextId));
		}
		if (contextId == null) contextId = eventContextId;
	}
	
	private Task saveTaskEvent(final Task event) {
		matchAndUpdateTaskAndContextIds(event.getId(), event.getContextId());
		saveTask(event);
		return event;
	}
	
	private Task saveTaskEvent(final TaskStatusUpdateEvent event) {
		matchAndUpdateTaskAndContextIds(event.getTaskId(), event.getContextId());
		Task task = ensureTask(event);
		LOGGER.fine(String.format("Updating task %s status to: %s", task.getId(), event.getStatus().toString()));
		List<Message> history = new ArrayList<>();
		if (task.getStatus().message() != null) {
			if (task.getHistory() != null && task.getHistory().length > 0) {
				history.addAll(Arrays.asList(task.getHistory()));
			}
			history.add(task.getStatus().message());
		}
		
		if (event.getMetadata() != null) {
			task.setMetadata(event.getMetadata());
		}
		
		task = new Task.Builder(task).status(event.getStatus()).history(history).build();
		saveTask(task);
		return task;
	}
	
	private Task saveTaskEvent(final TaskArtifactUpdateEvent event) {
		matchAndUpdateTaskAndContextIds(event.getTaskId(), event.getContextId());
		Task task = ensureTask(event);
		LOGGER.fine(String.format("Appending artifact to task %s", task.getId()));
        Helpers.appendArtifactToTask(task, event);
		saveTask(task);
		return task;
	}
	
	private Task ensureTask(final TaskStatusUpdateEvent event) {
		return ensureTask(event.getTaskId(), event.getContextId());
	}
	
	private Task ensureTask(final TaskArtifactUpdateEvent event) {
		return ensureTask(event.getTaskId(), event.getContextId());
	}
	
	/**
	 * Processes an event, updates the task state if applicable, stores it, and returns the event.
	 * 
	 * If the event is task-related ({@link Task}, {@link TaskStatusUpdateEvent}, {@link TaskArtifactUpdateEvent}),
     * the internal task state is updated and persisted.
     * 
	 * @param event The event object received from the agent.
	 * @return The same event object that was processed.
	 */
	public Event process(final Event event) {
		if (event instanceof Task task) {
			saveTaskEvent(task);
		} else if (event instanceof TaskStatusUpdateEvent tsue) {
			saveTaskEvent(tsue);
		} else if (event instanceof TaskArtifactUpdateEvent taue) {
			saveTaskEvent(taue);
		}
		
		return event;
	}
	
	private Task ensureTask(final String eventTaskId, final String eventContextId) {
		Task task = currentTask;
		if (task == null && taskId != null) {
			LOGGER.fine(String.format("Attempting to retrieve existing task with id: %s", taskId));
			task = taskStore.load(taskId, context);
		}
		
		if (task == null) {
			LOGGER.info(String.format("Task not found or taskId not set. Creating new task for event (taskId: %s, contextId: %s).", eventTaskId, eventContextId));
			task = createNewTaskObject(eventTaskId, eventContextId);
			saveTask(task);
		}
		
		return task;
	}
	
	private void saveTask(final Task task) {
		LOGGER.fine(String.format("Saving task with id: %s", task.getId()));
		taskStore.save(task, context);
		currentTask = task;
		if (taskId == null) {
			LOGGER.info(String.format("New task created with id: %s", task.getId()));
			taskId = task.getId();
			contextId = task.getContextId();
		}
	}
	
	/**
	 * Initializes a new task object in memory.
	 * 
	 * @param taskId The ID for the new task.
	 * @param contextId The context ID for the new task.
	 * @return  A new {@link Task} object with initial status and potentially the initial message in history.
	 */
	private Task createNewTaskObject(final String taskId, final String contextId) {
		LOGGER.info(String.format("Initializing new Task object with taskId: %s, contextId: %s.", taskId, contextId));
		Message[] history = initialMessage != null ? new Message [] { initialMessage } : new Message[] {};
		Task task = new Task(taskId, contextId, new TaskStatus(TaskState.SUBMITTED));
		task.setHistory(history);
		return task;
	}
	
	/**
	 * Updates a task object in memory by adding a new message to its history.
	 *
     *   If the task has a message in its current status, that message is moved
     *   to the history first.
     *   
	 * @param message The new {@link Message} to add to the history.
	 * @param task The {@link Task} object to update.
	 * @return The updated {@link Task} object (updated in-place).
	 */
	public Task updateWithMessage(final Message message, final Task task) {
		List<Message> history = task.getHistory() != null && task.getHistory().length > 0 ? new ArrayList<>(List.of(task.getHistory())) : new ArrayList<>();
		Task.Builder taskBuilder = new Task.Builder(task);
		if (task.getStatus().message() != null) {
			history.add(task.getStatus().message());
			taskBuilder.status(TaskStatus.create(task.getStatus().state(), null, task.getStatus().timestamp()));
		}
		history.add(message);
		return (currentTask = taskBuilder.history(history).build());
	}
}
