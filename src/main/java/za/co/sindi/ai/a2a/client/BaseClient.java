/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;
import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.client.transports.ClientTransport;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Kind;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.MessageSendConfiguration;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.StreamingKind;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;
import za.co.sindi.ai.a2a.types.UnsupportedOperationError;
import za.co.sindi.ai.a2a.types.UpdateEvent;
import za.co.sindi.commons.util.Either;

/**
 * Base implementation of the A2A client, containing transport-independent logic.
 * 
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class BaseClient extends Client {
	
	private AgentCard card;
	private ClientConfig config;
	private ClientTransport transport;

	/**
	 * 
	 * @param card
	 * @param config
	 * @param transport
	 * @param consumers
	 * @param interceptors
	 */
	public BaseClient(AgentCard card, ClientConfig config, ClientTransport transport, List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers,
			List<ClientCallInterceptor> interceptors) {
		super(consumers, interceptors);
		this.card = Objects.requireNonNull(card, "An Agent Card is required.");
		this.config = config;
		this.transport = transport;
	}

	@Override
	public void sendMessage(Message request, ClientCallContext context, Map<String, Object> requestMetadata) {
		// TODO Auto-generated method stub
		MessageSendConfiguration config = new MessageSendConfiguration(this.config.getAcceptedOutputModes() == null ? null : this.config.getAcceptedOutputModes().toArray(new String[this.config.getAcceptedOutputModes().size()]),
				!this.config.isPolling(), null, this.config.getPushNotificationConfigs() == null || this.config.getPushNotificationConfigs().isEmpty() ? null : this.config.getPushNotificationConfigs().get(0));
		
		MessageSendParams params = new MessageSendParams(config, request, requestMetadata);
		
		if (!this.config.isStreaming() || !card.getCapabilities().streaming()) {
			Kind response = transport.sendMessage(params, context);
			if (response instanceof Task task) consume(new ClientEvent(task, null), card);
			else if (response instanceof Message message) consume(message, card);
		} else {
			ClientTaskManager tracker = new ClientTaskManager();
			AtomicBoolean firstEvent = new AtomicBoolean(true);
			Consumer<StreamingKind> eventStream = event -> {
				if (firstEvent.get()) {
					firstEvent.set(false);
					if (event instanceof Message message) {
						consume(message, card);
					} else {
						processEvent(tracker, (Event)event);
					}
				} else {
					processEvent(tracker, (Event)event);
				}
			};
			transport.sendMessageStream(params, eventStream, null, context);
		}
	}
	
	private ClientEvent processEvent(ClientTaskManager tracker, Event event) {
		if (event instanceof Message) {
			throw new A2AClientInvalidStateError("received a streamed Message from server after first response; this is not supported.");
		}
		
		tracker.process(event);
		Task task = tracker.getTaskOrThrow();
		UpdateEvent update = event instanceof Task ? null : (UpdateEvent)event;
		ClientEvent clientEvent = new ClientEvent(task, update);
		consume(clientEvent, card);
		return clientEvent;		
	}

	@Override
	public Task getTask(TaskQueryParams request, ClientCallContext context) {
		// TODO Auto-generated method stub
		return transport.getTask(request, context);
	}

	@Override
	public Task cancelTask(TaskIdParams request, ClientCallContext context) {
		// TODO Auto-generated method stub
		return transport.cancelTask(request, context);
	}

	@Override
	public TaskPushNotificationConfig setTaskCallback(TaskPushNotificationConfig request, ClientCallContext context) {
		// TODO Auto-generated method stub
		return transport.setTaskCallback(request, context);
	}

	@Override
	public TaskPushNotificationConfig getTaskCallback(GetTaskPushNotificationConfigParams request, ClientCallContext context) {
		// TODO Auto-generated method stub
		return transport.getTaskCallback(request, context);
	}

	@Override
	public void resusbcribe(TaskIdParams request, ClientCallContext context) {
		// TODO Auto-generated method stub
		if (!config.isStreaming() || !card.getCapabilities().streaming()) {
            throw new A2AClientJSONRPCError(new UnsupportedOperationError("Client and/or server does not support resubscription"));
        }
		
		ClientTaskManager tracker = new ClientTaskManager();
		Consumer<StreamingKind> eventStream = event -> {
			processEvent(tracker, (Event)event);
		};
		transport.resubscribe(request, eventStream, null, context);
	}

	/**
	 * Retrieves the agent's card.
	 * 
     * This will fetch the authenticated card if necessary and update the
     * client's internal state with the new card.
	 */
	@Override
	public AgentCard getAgentCard(ClientCallContext context) {
		// TODO Auto-generated method stub
		this.card = transport.getCard(context);
		return this.card;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		transport.close();
	}
}
