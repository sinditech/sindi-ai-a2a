/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;
import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.MessageSendConfiguration;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;
import za.co.sindi.ai.a2a.types.UpdateEvent;
import za.co.sindi.commons.util.Either;

/**
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public abstract class Client implements AutoCloseable {

	protected final List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers;
	protected final List<ClientCallInterceptor> interceptors;
	
	/**
	 * @param consumers
	 * @param interceptors
	 */
	protected Client(List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers,
			List<ClientCallInterceptor> interceptors) {
		super();
		this.consumers = consumers == null ? new ArrayList<>() : new ArrayList<>(consumers);
		this.interceptors = interceptors == null ? new ArrayList<>() : new ArrayList<>(interceptors);
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * This will automatically use the streaming or non-streaming approach
	 * as supported by the server and the client config. Client will
	 * aggregate update events and return an iterator of ({@link Task}, {@link UpdateEvent})
	 * pairs, or a `Message`. Client will also send these values to any
	 * configured {@link Consumer}'s in the client.
        
	 * @param request The message to send to the agent.
	 */
	public void sendMessage(final Message request) {
		sendMessage(request, null);
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * This will automatically use the streaming or non-streaming approach
	 * as supported by the server and the client config. Client will
	 * aggregate update events and return an iterator of ({@link Task}, {@link UpdateEvent})
	 * pairs, or a `Message`. Client will also send these values to any
	 * configured {@link Consumer}'s in the client.
        
	 * @param request The message to send to the agent.
	 * @param context The client call context.
	 */
	public void sendMessage(final Message request, final ClientCallContext context) {
		sendMessage(request, context, null);
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * This will automatically use the streaming or non-streaming approach
	 * as supported by the server and the client config. Client will
	 * aggregate update events and return an iterator of ({@link Task}, {@link UpdateEvent})
	 * pairs, or a `Message`. Client will also send these values to any
	 * configured {@link Consumer}'s in the client.
        
	 * @param request The message to send to the agent.
	 * @param context The client call context.
	 * @param requestMetadata Extensions Metadata attached to the request.
	 */
	public void sendMessage(final Message request, final ClientCallContext context, final Map<String, Object> requestMetadata) {
		sendMessage(request, context, requestMetadata, null);
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * This will automatically use the streaming or non-streaming approach
	 * as supported by the server and the client config. Client will
	 * aggregate update events and return an iterator of ({@link Task}, {@link UpdateEvent})
	 * pairs, or a `Message`. Client will also send these values to any
	 * configured {@link Consumer}'s in the client.
	 * 
	 * @param request The message to send to the agent.
	 * @param context The client call context.
	 * @param requestMetadata Extensions Metadata attached to the request.
	 * @param extensions List of extensions to be activated.
	 */
	public void sendMessage(final Message request, final ClientCallContext context, final Map<String, Object> requestMetadata, List<String> extensions) {
		sendMessage(request, context, null, requestMetadata, extensions);
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * This will automatically use the streaming or non-streaming approach
	 * as supported by the server and the client config. Client will
	 * aggregate update events and return an iterator of ({@link Task}, {@link UpdateEvent})
	 * pairs, or a `Message`. Client will also send these values to any
	 * configured {@link Consumer}'s in the client.
	 * 
	 * @param request The message to send to the agent.
	 * @param context The client call context.
	 * @param configuration Optional per-call overrides for message sending behavior.
	 * @param requestMetadata Extensions Metadata attached to the request.
	 * @param extensions List of extensions to be activated.
	 */
	public abstract void sendMessage(Message request, ClientCallContext context, final MessageSendConfiguration configuration, Map<String, Object> requestMetadata, final List<String> extensions);
	
	/**
	 * Retrieves the current state and history of a specific task.
	 * 
	 * @param request
	 * @return
	 */
	public Task getTask(final TaskQueryParams request) {
		return getTask(request, null);
	}
	
	/**
	 * Retrieves the current state and history of a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public Task getTask(final TaskQueryParams request, final ClientCallContext context) {
		return getTask(request, context, null);
	}
	
	/**
	 * Retrieves the current state and history of a specific task.
	 * 
	 * @param request
	 * @param context
	 * @param extensions
	 * @return
	 */
	public abstract Task getTask(final TaskQueryParams request, final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Requests the agent to cancel a specific task.
	 * 
	 * @param request
	 * @return
	 */
	public Task cancelTask(final TaskIdParams request) {
		return cancelTask(request, null);
	}
	
	/**
	 * Requests the agent to cancel a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public Task cancelTask(final TaskIdParams request, final ClientCallContext context) {
		return cancelTask(request, context, null);
	}
	
	/**
	 * Requests the agent to cancel a specific task.
	 * 
	 * @param request
	 * @param context
	 * @param extensions
	 * @return
	 */
	public abstract Task cancelTask(final TaskIdParams request, final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Sets or updates the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @return
	 */
	public TaskPushNotificationConfig setTaskCallback(final TaskPushNotificationConfig request) {
		return setTaskCallback(request, null);
	}
	
	/**
	 * Sets or updates the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public TaskPushNotificationConfig setTaskCallback(final TaskPushNotificationConfig request, final ClientCallContext context) {
		return setTaskCallback(request, context, null);
	}
	
	/**
	 * Sets or updates the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @param extensions
	 * @return
	 */
	public abstract TaskPushNotificationConfig setTaskCallback(final TaskPushNotificationConfig request, final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Retrieves the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @return
	 */
	public TaskPushNotificationConfig getTaskCallback(final GetTaskPushNotificationConfigParams request) {
		return getTaskCallback(request, null);
	}
	
	/**
	 * Retrieves the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public TaskPushNotificationConfig getTaskCallback(final GetTaskPushNotificationConfigParams request, final ClientCallContext context) {
		return getTaskCallback(request, context, null);
	}
	
	/**
	 * Retrieves the push notification configuration for a specific task.
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public abstract TaskPushNotificationConfig getTaskCallback(final GetTaskPushNotificationConfigParams request, final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Resubscribes to a task's event stream.
	 *         
	 * @param request
	 * @param context
	 */
	public void resusbcribe(final TaskIdParams request, final ClientCallContext context) {
		resusbcribe(request, context, null);
	}
	
	/**
	 * Resubscribes to a task's event stream.
	 *         
	 * @param request
	 * @param context
	 */
	public abstract void resusbcribe(final TaskIdParams request, final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Retrieves the agent's card.
	 * 
	 * @return
	 */
	public AgentCard getAgentCard() {
		return getAgentCard(null);
	}
	
	/**
	 * Retrieves the agent's card.
	 * 
	 * @param context
	 * @return
	 */
	public AgentCard getAgentCard(final ClientCallContext context) {
		return getAgentCard(context, null);
	}
	
	/**
	 * Retrieves the agent's card.
	 * 
	 * @param context
	 * @return
	 */
	public abstract AgentCard getAgentCard(final ClientCallContext context, final List<String> extensions);
	
	/**
	 * Attaches additional consumers to the {@link Client}.
	 * @param consumer
	 */
	public void addEventConsumer(final BiConsumer<Either<ClientEvent, Message>, AgentCard> consumer) {
		if (consumer != null) consumers.add(consumer);
	}
	
	/**
	 * Attaches additional interceptor to the {@link Client}.
	 * 
	 * @param interceptor
	 */
	public void addInterceptor(final ClientCallInterceptor interceptor) {
		if (interceptor != null) interceptors.add(interceptor);
	}
	
	/**
	 * Processes the event via all the registered consumers.
	 * 
	 * @param event
	 * @param card
	 */
	public void consume(final ClientEvent event, final AgentCard card) {
		if (event == null) return;
		
		for (BiConsumer<Either<ClientEvent, Message>, AgentCard> consumer: consumers) {
			consumer.accept(Either.left(event), card);
		}
	}
	
	/**
	 * Processes the message via all the registered consumers.
	 * 
	 * @param message
	 * @param card
	 */
	public void consume(final Message message, final AgentCard card) {
		if (message == null) return;
		
		for (BiConsumer<Either<ClientEvent, Message>, AgentCard> consumer: consumers) {
			consumer.accept(Either.right(message), card);
		}
	}
}
