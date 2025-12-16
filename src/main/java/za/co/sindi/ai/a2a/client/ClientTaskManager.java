/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.Task.Builder;
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
    
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class ClientTaskManager {
	private static final Logger LOGGER = Logger.getLogger(ClientTaskManager.class.getName());
	
	private Task currentTask;
	private String taskId;
	private String contextId;
	
	/**
	 * Retrieves the current task object, either from memory.
	 * 
	 * If <code>taskId</code> is set, it returns If <code>currentTask</code> otherwise <code>null</code>.
	 * 
	 * @return The {@link Task} object if found, otherwise <code>null</code>.
	 */
	public Task getTask() {
		if (taskId == null || taskId.isEmpty()) {
			LOGGER.fine("Task ID is not set, cannot get task.");
			return null;
		}
		
		return currentTask;
	}
	
	/**
	 * Retrieves the current task object.
	 * 
	 * @return The {@link Task} object.
	 * @throws A2AClientInvalidStateError If there is no current known Task.
	 */
	public Task getTaskOrThrow() {
		if (getTask() == null) {
			throw new A2AClientInvalidStateError("No current task.");
		}
		
		return currentTask;
	}
	
	private Task saveTaskEvent(final Task task) {
		if (task != null) {
			if (currentTask != null) {
				throw new A2AClientInvalidArgsError("Task is already set, create new manager for new tasks.");
			}
				
			saveTask(task);
		}
			
		return task;
	}
	
	/**
	 * Processes a task-related event (Task, Status, Artifact) and saves the updated task state.
	 * 
	 * Ensures task and context IDs match or are set from the event.
	 * 
	 * @param event The task-related event ({@link TaskStatusUpdateEvent}.
	 * @return The updated {@link Task} object after processing the event.
	 */
	private Task saveTaskEvent(final TaskStatusUpdateEvent event) {
		if (taskId == null) taskId = event.getTaskId();
		if (contextId == null) contextId = event.getContextId();
		
		LOGGER.fine(String.format("Processing save of task event of type %s for task_id: %s", event.getClass().getSimpleName(), event.getTaskId()));
		Task task = currentTask;
		if (task == null) {
			task = new Task(taskId, contextId == null ? "" : contextId, new TaskStatus(TaskState.UNKNOWN));
		}
		
		LOGGER.fine(String.format("Updating task %s status to: %s", event.getTaskId(), event.getStatus().state()));
		if (event.getStatus().message() != null) {
			if (task.getHistory() == null) {
				task.setHistory(new Message[] { event.getStatus().message() } );
			} else {
				List<Message> history = new ArrayList<>(List.of(task.getHistory()));
				history.add(event.getStatus().message());
				task.setHistory(history.toArray(new Message[history.size()]));
			}
		}
		
		if (event.getMetadata() != null) {
			task.setMetadata(task.getMetadata() == null ? new HashMap<>() : new HashMap<>(task.getMetadata()));
			task.getMetadata().putAll(event.getMetadata());
		}
		
		task = new Builder(task).status(event.getStatus()).build();
		currentTask = task;
		return task;
	}
	
	/**
	 * Processes a task-related event (Task, Status, Artifact) and saves the updated task state.
	 * 
	 * Ensures task and context IDs match or are set from the event.
	 * 
	 * @param event The task-related event {@link TaskArtifactUpdateEvent}).
	 * @return The updated {@link Task} object after processing the event.
	 */
	private Task saveTaskEvent(final TaskArtifactUpdateEvent event) {
		if (taskId == null) taskId = event.getTaskId();
		if (contextId == null) contextId = event.getContextId();
		
		LOGGER.fine(String.format("Processing save of task event of type %s for task_id: %s", event.getClass().getSimpleName(), event.getTaskId()));
		Task task = currentTask;
		if (task == null) {
			task = new Task(taskId, contextId == null ? "" : contextId, new TaskStatus(TaskState.UNKNOWN));
		}
		
		LOGGER.fine(String.format("Appending artifact to task %s", event.getTaskId()));
		Helpers.appendArtifactToTask(task, event);
		currentTask = task;
		return task;
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
	
	/**
	 * Updates a task object adding a new message to its history.
	 * 
	 * If the task has a message in its current status, that message is moved
	 * to the history first.
	 * 
	 * @param message  The new {@link Message} to add to the history.
	 * @param task The {@link Task} object to update.
	 * @return The updated {@link Task} object (updated in-place).
	 */
	public Task updateWithMessage(final Message message, final Task task) {
		Task.Builder taskBuilder = new Task.Builder(task);
		List<Message> history = task.getHistory() != null && task.getHistory().length > 0 ? new ArrayList<>(List.of(task.getHistory())) : new ArrayList<>();
		if (task.getStatus().message() != null) {
			history.add(task.getStatus().message());
			taskBuilder.status(TaskStatus.create(task.getStatus().state(), null, task.getStatus().timestamp()));
		}
		
		history.add(message);
		return (currentTask = taskBuilder.history(history).build());
	}
	
	/**
	 * Saves the given task to the <code>currentTask</code> and updated <code>taskId</code> and <code>contextId</code>.
	 * 
	 * @param task The {@link Task} object to save.
	 */
	private void saveTask(final Task task) {
		LOGGER.fine(String.format("Saving task with id: %s", task.getId()));
		currentTask = task;
		if (taskId == null) {
			LOGGER.info(String.format("New task created with id: %s", task.getId()));
			taskId = task.getId();
			contextId = task.getContextId();
		}
	}
}
