/**
 * 
 */
package za.co.sindi.ai.a2a.server.requesthandlers;

import java.util.List;
import java.util.concurrent.Flow.Publisher;

import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.DeleteTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.ListTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;
import za.co.sindi.ai.a2a.types.UnsupportedOperationError;

/**
 * A2A request handler interface.
 * 
 * This interface defines the methods that an A2A server implementation must
 * provide to handle incoming JSON-RPC requests.
 *   
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public interface RequestHandler {

	/**
	 * Handles the 'tasks/get' method.
	 *
     * Retrieves the state and history of a specific task.
     *    
	 * @param params Parameters specifying the task ID and optionally history length.
	 * @param context Context provided by the server.
	 * @return The {@link Task} object if found, otherwise <code>null</code>.
	 */
	public Task onGetTask(final TaskQueryParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/cancel' method.
	 * 
	 * Requests the agent to cancel an ongoing task.
	 * 
	 * @param params Parameters specifying the task ID.
	 * @param context Context provided by the server.
	 * @return The {@link Task} object with its status updated to canceled, or <code>null</code> if the task was not found.
	 */
	public Task onCancelTask(final TaskIdParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'message/send' method (non-streaming).
	 * 
	 * Sends a message to the agent to create, continue, or restart a task,
	 * and waits for the final result ({@link Task} or {@link Message}).
	 *         
	 * @param params Parameters including the message and configuration.
	 * @param context Context provided by the server.
	 * @return The final {@link Task} object or a final {@link Message} object.
	 */
	public Event onMessageSend(final MessageSendParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'message/send' method (streaming).
	 * 
	 * Sends a message to the agent and yields stream events as they are
	 * produced ({@link Task} updates, {@link Message} chunks, {@link Artifact} updates).
	 *         
	 * @param params Parameters including the message and configuration.
	 * @param context Context provided by the server.
	 * @return {@link Event} objects from the agent's execution.
	 */
	public Publisher<Event> onMessageSendStream(final MessageSendParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/pushNotificationConfig/set' method.
	 * 
     * Sets or updates the push notification configuration for a task.
     *     
	 * @param params Parameters including the task ID and push notification configuration.
	 * @param context Context provided by the server.
	 * @return The provided {@link TaskPushNotificationConfig} upon success.
	 */
	public TaskPushNotificationConfig onSetTaskPushNotificationConfig(final TaskPushNotificationConfig params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/pushNotificationConfig/get' method.
	 * 
     * Retrieves the current push notification configuration for a task.
     *     
	 * @param params Parameters including the task ID.
	 * @param context Context provided by the server.
	 * @return The {@link TaskPushNotificationConfig} for the task.
	 */
	public TaskPushNotificationConfig onGetTaskPushNotificationConfig(final TaskIdParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/pushNotificationConfig/get' method.
	 * 
     * Retrieves the current push notification configuration for a task.
     *     
	 * @param params Parameters including the task ID.
	 * @param context Context provided by the server.
	 * @return The {@link TaskPushNotificationConfig} for the task.
	 */
	public TaskPushNotificationConfig onGetTaskPushNotificationConfig(final GetTaskPushNotificationConfigParams params, final ServerCallContext context);
	
	/**
	 * "Handles the 'tasks/resubscribe' method.
	 * 
	 * Allows a client to re-subscribe to a running streaming task's event stream.
	 * 
	 * @param params Parameters including the task ID.
	 * @param context Context provided by the server.
	 * @return {@link Event} objects from the agent's ongoing execution for the specified task.
	 * @throws {@link A2AServerError}({@link UnsupportedOperationError}): By default, if not implemented.
	 */
	public Publisher<Event> onResubmitToTask(final TaskIdParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/pushNotificationConfig/list' method.
	 * 
	 * Retrieves the current push notification configurations for a task.
	 * 
	 * @param params Parameters including the task ID.
	 * @param context Context provided by the server.
	 * @return The List&lt;{@link TaskPushNotificationConfig}&gt; for the task.
	 */
	public List<TaskPushNotificationConfig> onListTaskPushNotificationConfig(final ListTaskPushNotificationConfigParams params, final ServerCallContext context);
	
	/**
	 * Handles the 'tasks/pushNotificationConfig/delete' method.
	 * 
	 * Deletes a push notification configuration associated with a task.
	 * 
	 * @param params Parameters including the task ID.
	 * @param context Context provided by the server.
	 */
	public void onDeleteTaskPushNotificationConfig(final DeleteTaskPushNotificationConfigParams params, final ServerCallContext context);
}
