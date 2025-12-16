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
	public void sendMessage(Message request, ClientCallContext context, final MessageSendConfiguration configuration, Map<String, Object> requestMetadata, final List<String> extensions) {
		// TODO Auto-generated method stub
		MessageSendConfiguration baseConfig = new MessageSendConfiguration(this.config.acceptedOutputModes() == null ? null : this.config.acceptedOutputModes().toArray(new String[this.config.acceptedOutputModes().size()]),
				!this.config.polling(), null, this.config.pushNotificationConfigs() == null || this.config.pushNotificationConfigs().isEmpty() ? null : this.config.pushNotificationConfigs().get(0));
		
		MessageSendConfiguration config = configuration == null ? baseConfig : configuration;
		
		MessageSendParams params = new MessageSendParams(config, request, requestMetadata);
		
		if (!this.config.streaming() || !card.getCapabilities().streaming()) {
			Kind response = transport.sendMessage(params, context, extensions);
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
						processResponse(tracker, (Event)event);
					}
				} else {
					processResponse(tracker, (Event)event);
				}
			};
			transport.sendMessageStream(params, eventStream, null, context, extensions);
		}
	}
	
	private ClientEvent processResponse(ClientTaskManager tracker, Event event) {
		if (event instanceof Message) {
			throw new A2AClientInvalidStateError("Received a streamed Message from server after first response; this is not supported.");
		}
		
		tracker.process(event);
		Task task = tracker.getTaskOrThrow();
		UpdateEvent update = event instanceof Task ? null : (UpdateEvent)event;
		ClientEvent clientEvent = new ClientEvent(task, update);
		consume(clientEvent, card);
		return clientEvent;		
	}

	@Override
	public Task getTask(TaskQueryParams request, ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		return transport.getTask(request, context, extensions);
	}

	@Override
	public Task cancelTask(TaskIdParams request, ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		return transport.cancelTask(request, context, extensions);
	}

	@Override
	public TaskPushNotificationConfig setTaskCallback(TaskPushNotificationConfig request, ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		return transport.setTaskCallback(request, context, extensions);
	}

	@Override
	public TaskPushNotificationConfig getTaskCallback(GetTaskPushNotificationConfigParams request, ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		return transport.getTaskCallback(request, context, extensions);
	}

	@Override
	public void resusbcribe(TaskIdParams request, ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		if (!config.streaming() || !card.getCapabilities().streaming()) {
            throw new A2AClientJSONRPCError(new UnsupportedOperationError("Client and/or server does not support resubscription."));
        }
		
		ClientTaskManager tracker = new ClientTaskManager();
		Consumer<StreamingKind> eventStream = event -> {
			processResponse(tracker, (Event)event);
		};
		transport.resubscribe(request, eventStream, null, context, extensions);
	}

	/**
	 * Retrieves the agent's card.
	 * 
     * This will fetch the authenticated card if necessary and update the
     * client's internal state with the new card.
	 */
	@Override
	public AgentCard getAgentCard(ClientCallContext context, final List<String> extensions) {
		// TODO Auto-generated method stub
		this.card = transport.getCard(context, extensions);
		return this.card;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		transport.close();
	}
}
