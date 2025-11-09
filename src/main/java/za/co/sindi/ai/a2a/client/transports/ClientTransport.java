/**
 * 
 */
package za.co.sindi.ai.a2a.client.transports;

import java.util.function.Consumer;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Kind;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.StreamingKind;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;

/**
 * Abstract base class for a client transport.
 * 
 * @author Buhake Sindi
 * @since 01 November 2025
 */
public interface ClientTransport extends AutoCloseable {

	/**
	 * Sends a non-streaming message request to the agent.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public Kind sendMessage(final MessageSendParams request, final ClientCallContext context);
	
	/**
	 * Sends a streaming message request to the agent and yields responses as they arrive.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public void sendMessageStream(final MessageSendParams request, final Consumer<StreamingKind> eventDataConsumer, final Consumer<Throwable> eventErrorConsumer, final ClientCallContext context);
//	public Publisher<StreamingKind> sendMessageStream(final MessageSendParams request, final ClientCallContext context);
	
	/**
	 * Retrieves the current state and history of a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public Task getTask(final TaskQueryParams request, final ClientCallContext context);
	
	/**
	 * Requests the agent to cancel a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public Task cancelTask(final TaskIdParams request, final ClientCallContext context);
	
	/**
	 * Sets or updates the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public TaskPushNotificationConfig setTaskCallback(final TaskPushNotificationConfig request, final ClientCallContext context);
	
	/**
	 * Retrieves the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public TaskPushNotificationConfig getTaskCallback(final GetTaskPushNotificationConfigParams request, final ClientCallContext context);
	
	/**
	 * Sends a streaming message request to the agent and yields responses as they arrive.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public void resubscribe(final TaskIdParams request, final Consumer<StreamingKind> eventDataConsumer, final Consumer<Throwable> eventErrorConsumer, final ClientCallContext context);
//	public StreamingKind resubscribe(final TaskIdParams request, final ClientCallContext context);
	
	/**
	 * Retrieves the AgentCard.
	 * 
	 * @param context
	 * @return
	 */
	public AgentCard getCard(final ClientCallContext context);
	
	/**
	 * Closes the transport.
	 */
	public void close();
}
