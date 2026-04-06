/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.Task.Builder;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatus;
import za.co.sindi.ai.a2a.types.TextPart;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 30 October 2025
 */
public class Tasks {

	private Tasks() {
		throw new AssertionError("Private constructor.");
	}
	
	/**
	 * Creates a new Task object from an initial user message.
	 *
     * <p />Generates task and context IDs if not provided in the message.
     * 
	 * @param request The initial {@link Message} object from the user. 
	 * @return A new {@link Task} object initialized with 'submitted' status and the input message in history.
	 */
	public static Task newTask(final Message request) {
		Preconditions.checkState(request.getRole() != null, "Message role cannot be null.");
		Preconditions.checkState(request.getParts() != null && request.getParts().length > 0, "Message parts cannot be empty.");
//		if (request.getRole() == null) throw new IllegalStateException("Message role cannot be null.");
//		if (request.getParts() == null || request.getParts().length == 0) throw new IllegalStateException("Message parts cannot be empty.");
		for (Part part : request.getParts()) {
			if (part instanceof TextPart textPart && Strings.isNullOrEmpty(textPart.getText())) throw new IllegalStateException("TextPart content cannot be empty.");
		}
		
		return new Task.Builder()
					   .status(new TaskStatus(TaskState.SUBMITTED))
					   .id(Optional.ofNullable(request.getTaskId()).orElse(UUID.randomUUID().toString()))
					   .contextId(Optional.ofNullable(request.getContextId()).orElse(UUID.randomUUID().toString()))
					   .history(Arrays.asList(request))
					   .build();
	}
	
	/**
	 * Creates a Task object in the 'completed' state.
	 * 
	 * @param taskId The ID of the task.
	 * @param contextId The context ID of the task.
	 * @param artifacts  A list of {@link Artifact} objects produced by the task.
	 * @param histories An optional list of {@link Message} objects representing the task history.
	 * @return  A {@link Task} object with status set to 'completed'.
	 */
	public static Task completedTask(final String taskId, final String contextId, final List<Artifact> artifacts, final List<Message> histories) {
		Preconditions.checkState(artifacts != null && !artifacts.isEmpty(), "Artifacts must be a non-empty list of Artifact objects.");
		
		return new Task.Builder()
				   .status(new TaskStatus(TaskState.COMPLETED))
				   .id(taskId)
				   .contextId(contextId)
				   .artifacts(artifacts)
				   .history(histories == null ? List.of() : histories)
				   .build();
	}
	
	/**
	 * Applies history_length parameter on task and returns a new task object.
	 * 
	 * @param task The original task object with complete history
	 * @param historyLength History length configuration value
	 * @return  A new task object with limited history
	 */
	public static Task applyHistoryLength(final Task task, final Integer historyLength) {
		
		if (historyLength != null && task.getHistory() != null && task.getHistory().length > 0) {
			List<Message> histories = List.of(task.getHistory());
			List<Message> limitedHistory =  (historyLength > 0) ? histories.subList(task.getHistory().length - historyLength, task.getHistory().length) : Collections.emptyList();
			
			return new Builder(task).history(limitedHistory.toArray(new Message[limitedHistory.size()])).build();
		}
		
		return task;
	}
}
