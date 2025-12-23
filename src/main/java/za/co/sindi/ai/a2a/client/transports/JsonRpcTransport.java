/**
 * 
 */
package za.co.sindi.ai.a2a.client.transports;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow.Publisher;
import java.util.function.Consumer;
import java.util.function.Function;

import za.co.sindi.ai.a2a.client.A2ACardResolver;
import za.co.sindi.ai.a2a.client.A2AClientHTTPError;
import za.co.sindi.ai.a2a.client.A2AClientJSONRPCError;
import za.co.sindi.ai.a2a.client.A2AClientTimeoutError;
import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;
import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor.RequestPayloadAndKeywordArguments;
import za.co.sindi.ai.a2a.extensions.A2AExtensions;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.CancelTaskRequest;
import za.co.sindi.ai.a2a.types.CancelTaskSuccessResponse;
import za.co.sindi.ai.a2a.types.GetAuthenticatedExtendedCardRequest;
import za.co.sindi.ai.a2a.types.GetAuthenticatedExtendedCardSuccessResponse;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigRequest;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigSuccessResponse;
import za.co.sindi.ai.a2a.types.GetTaskRequest;
import za.co.sindi.ai.a2a.types.GetTaskSuccessResponse;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse;
import za.co.sindi.ai.a2a.types.JSONRPCRequest;
import za.co.sindi.ai.a2a.types.JSONRPCResponse;
import za.co.sindi.ai.a2a.types.JSONRPCResultResponse;
import za.co.sindi.ai.a2a.types.Kind;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.RequestId;
import za.co.sindi.ai.a2a.types.SendMessageRequest;
import za.co.sindi.ai.a2a.types.SendMessageSuccessResponse;
import za.co.sindi.ai.a2a.types.SendStreamingMessageRequest;
import za.co.sindi.ai.a2a.types.SendStreamingMessageSuccessResponse;
import za.co.sindi.ai.a2a.types.SetTaskPushNotificationConfigRequest;
import za.co.sindi.ai.a2a.types.SetTaskPushNotificationConfigSuccessResponse;
import za.co.sindi.ai.a2a.types.StreamingKind;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;
import za.co.sindi.ai.a2a.types.TaskResubscriptionRequest;
import za.co.sindi.ai.a2a.utils.JsonUtils;
import za.co.sindi.commons.net.http.WithErrorBodyHandler;
import za.co.sindi.commons.net.sse.Event;
import za.co.sindi.commons.net.sse.EventHandler;
import za.co.sindi.commons.net.sse.MessageEvent;
import za.co.sindi.commons.net.sse.SSEEventLineSubscriber;
import za.co.sindi.commons.net.sse.SSEEventSubscriber;
import za.co.sindi.commons.util.Either;
import za.co.sindi.commons.utils.Preconditions;
import za.co.sindi.commons.utils.Strings;

/**
 * A JSON-RPC transport for the A2A client.
 * 
 * @author Buhake Sindi
 * @since 02 November 2025
 */
public class JsonRpcTransport implements ClientTransport {

	private final HttpClient httpClient;
	private final String agentUrl;
	private AgentCard agentCard;
	private final List<ClientCallInterceptor> interceptors;
	private final List<String> extensions;
	private boolean needsExtendedCard;
	
	/**
	 * @param httpClient
	 * @param agentUrl
	 * @param interceptors
	 */
	public JsonRpcTransport(HttpClient httpClient, String agentUrl, List<ClientCallInterceptor> interceptors) {
		this(httpClient, agentUrl, null, interceptors, null);
	}
	
	/**
	 * @param httpClient
	 * @param agentCard
	 * @param interceptors
	 */
	public JsonRpcTransport(HttpClient httpClient, AgentCard agentCard, List<ClientCallInterceptor> interceptors) {
		this(httpClient, null, agentCard, interceptors, null);
	}
	
	/**
	 * @param httpClient
	 * @param agentUrl
	 * @param agentCard
	 * @param interceptors
	 * @param extensions;
	 */
	public JsonRpcTransport(HttpClient httpClient, String agentUrl, AgentCard agentCard,
			List<ClientCallInterceptor> interceptors, final List<String> extensions) {
		super();
		this.httpClient = Objects.requireNonNull(httpClient, "An HTTP Client is required.");
		this.agentUrl = agentUrl != null && !agentUrl.isEmpty() ? agentUrl : agentCard != null ? agentCard.getUrl() : null;
		this.agentCard = agentCard;
		this.interceptors = interceptors == null ? new ArrayList<>() : new ArrayList<>(interceptors);
		this.needsExtendedCard = agentCard != null && agentCard.getSupportsAuthenticatedExtendedCard() != null ? agentCard.getSupportsAuthenticatedExtendedCard() : true;
		if (this.agentUrl == null || this.agentUrl.isEmpty()) throw new IllegalArgumentException("Must provide either an agent card or an agent URL.");
		this.extensions = extensions;
	}
	
	private RequestPayloadAndKeywordArguments applyInterceptors(final String methodName, final /*Map<String, Object>*/ Object requestPayload, final Map<String, Object> httpKeywordArguments, final ClientCallContext context) {
		RequestPayloadAndKeywordArguments finalRequestPayloadAndKeywordArguments = new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
		for (ClientCallInterceptor interceptor : interceptors) {
			finalRequestPayloadAndKeywordArguments = interceptor.intercept(methodName, finalRequestPayloadAndKeywordArguments.payload(), finalRequestPayloadAndKeywordArguments.keywordArguments(), agentCard, context);
		}
		
		return finalRequestPayloadAndKeywordArguments;
	}
	
	private Map<String, Object> getHttpArguments(final ClientCallContext context) {
		return context == null ? null : context.state();
	}

	@Override
	public Kind sendMessage(MessageSendParams request, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A MessageSendParams request is required.");
		SendMessageRequest sendMessageRequest = new SendMessageRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(sendMessageRequest.getRequestMethod(), sendMessageRequest, modifiedKeywordArguments, context);
		SendMessageSuccessResponse response = sendRequest(sendMessageRequest, requestPayloadAndKeywordArguments.keywordArguments(), SendMessageSuccessResponse.class);
		return response.getResult();
	}

	@Override
	public void sendMessageStream(MessageSendParams request, Consumer<StreamingKind> eventDataConsumer,
			Consumer<Throwable> eventErrorConsumer, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A MessageSendParams request is required.");
		SendStreamingMessageRequest sendStreamingMessageRequest = new SendStreamingMessageRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		modifiedKeywordArguments.computeIfAbsent("headers", _ -> new ConcurrentHashMap<>(Map.of("Accept", "text/event-stream")));
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(sendStreamingMessageRequest.getRequestMethod(), sendStreamingMessageRequest, modifiedKeywordArguments, context);
		sendRequest(sendStreamingMessageRequest, requestPayloadAndKeywordArguments.keywordArguments(), eventDataConsumer, eventErrorConsumer, SendStreamingMessageSuccessResponse.class);
	}
	
	@Override
	public Task getTask(TaskQueryParams request, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A TaskQueryParams request is required.");
		GetTaskRequest getTaskRequest = new GetTaskRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(getTaskRequest.getRequestMethod(), getTaskRequest, modifiedKeywordArguments, context);
		GetTaskSuccessResponse response = sendRequest(getTaskRequest, requestPayloadAndKeywordArguments.keywordArguments(), GetTaskSuccessResponse.class);
		return response.getResult();
	}

	@Override
	public Task cancelTask(TaskIdParams request, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A TaskIdParams request is required.");
		CancelTaskRequest cancelTaskRequest = new CancelTaskRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(cancelTaskRequest.getRequestMethod(), cancelTaskRequest, modifiedKeywordArguments, context);
		CancelTaskSuccessResponse response = sendRequest(cancelTaskRequest, requestPayloadAndKeywordArguments.keywordArguments(), CancelTaskSuccessResponse.class);
		return response.getResult();
	}

	@Override
	public TaskPushNotificationConfig setTaskCallback(TaskPushNotificationConfig request, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A TaskPushNotificationConfig request is required.");
		SetTaskPushNotificationConfigRequest setTaskPushNotificationConfigRequest = new SetTaskPushNotificationConfigRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(setTaskPushNotificationConfigRequest.getRequestMethod(), setTaskPushNotificationConfigRequest, modifiedKeywordArguments, context);
		SetTaskPushNotificationConfigSuccessResponse response = sendRequest(setTaskPushNotificationConfigRequest, requestPayloadAndKeywordArguments.keywordArguments(), SetTaskPushNotificationConfigSuccessResponse.class);
		return response.getResult();
	}

	@Override
	public TaskPushNotificationConfig getTaskCallback(GetTaskPushNotificationConfigParams request, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A GetTaskPushNotificationConfigParams request is required.");
		GetTaskPushNotificationConfigRequest getTaskPushNotificationConfigRequest = new GetTaskPushNotificationConfigRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(getTaskPushNotificationConfigRequest.getRequestMethod(), getTaskPushNotificationConfigRequest, modifiedKeywordArguments, context);
		GetTaskPushNotificationConfigSuccessResponse response = sendRequest(getTaskPushNotificationConfigRequest, requestPayloadAndKeywordArguments.keywordArguments(), GetTaskPushNotificationConfigSuccessResponse.class);
		return response.getResult();
	}

	@Override
	public void resubscribe(TaskIdParams request, Consumer<StreamingKind> eventDataConsumer, Consumer<Throwable> eventErrorConsumer, ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		Preconditions.checkArgument(request != null, "A TaskIdParams request is required.");
		TaskResubscriptionRequest taskResubscriptionRequest = new TaskResubscriptionRequest(RequestId.of(UUID.randomUUID().toString()), request);
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(taskResubscriptionRequest.getRequestMethod(), taskResubscriptionRequest, modifiedKeywordArguments, context);
		sendRequest(taskResubscriptionRequest, requestPayloadAndKeywordArguments.keywordArguments(), eventDataConsumer, eventErrorConsumer, SendStreamingMessageSuccessResponse.class);
	}

	@Override
	public AgentCard getCard(ClientCallContext context, List<String> extensions) {
		// TODO Auto-generated method stub
		if (agentCard == null) {
			A2ACardResolver resolver = new A2ACardResolver(httpClient, agentUrl);
			agentCard = resolver.getAgentCard(null, getHttpArguments(context));
			if (agentCard.getSupportsAuthenticatedExtendedCard() != null) needsExtendedCard = agentCard.getSupportsAuthenticatedExtendedCard();
		}
		
		if (!needsExtendedCard) return agentCard;
		
		GetAuthenticatedExtendedCardRequest getAuthenticatedExtendedCardRequest = new GetAuthenticatedExtendedCardRequest(RequestId.of(UUID.randomUUID().toString()));
		Map<String, Object> modifiedKeywordArguments = A2AExtensions.updateExtensionHeader(getHttpArguments(context), extensions != null && !extensions.isEmpty() ? extensions : this.extensions);
		RequestPayloadAndKeywordArguments requestPayloadAndKeywordArguments = applyInterceptors(getAuthenticatedExtendedCardRequest.getRequestMethod(), getAuthenticatedExtendedCardRequest, modifiedKeywordArguments, context);
		GetAuthenticatedExtendedCardSuccessResponse response = sendRequest(getAuthenticatedExtendedCardRequest, requestPayloadAndKeywordArguments.keywordArguments(), GetAuthenticatedExtendedCardSuccessResponse.class);
		agentCard = response.getResult();
		needsExtendedCard = false;
		return agentCard;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		httpClient.close();
	}
	
	private <REQ extends JSONRPCRequest<?>> HttpRequest createHttpPOSTRequest(final REQ requestPayload, final Map<String, Object> httpKeywordArguments) {
		HttpRequest.Builder postRequestBuilder = HttpRequest.newBuilder(URI.create(agentUrl))
															 .header("Content-Type", "application/json")
															 .POST(BodyPublishers.ofString(JsonUtils.marshall(requestPayload)));
		if (httpKeywordArguments != null && httpKeywordArguments.containsKey("headers")) {
			@SuppressWarnings("unchecked")
			Map<String, String> headers = (Map<String, String>) httpKeywordArguments.get("headers");
			for (Entry<String, String> headerEntry : headers.entrySet()) {
				postRequestBuilder.header(headerEntry.getKey(), headerEntry.getValue());
			}
		}
		
		if (httpKeywordArguments.containsKey("timeout")) {
			postRequestBuilder.timeout((Duration) httpKeywordArguments.get("timeout"));
		}
	
		return postRequestBuilder.build();
	}
	
	private <REQ extends JSONRPCRequest<?>, RES extends JSONRPCResponse> RES sendRequest(final REQ requestPayload, final Map<String, Object> httpKeywordArguments, final Class<RES> expectedResultType) {
		try {
			HttpResponse<String> response = httpClient.send(createHttpPOSTRequest(requestPayload, httpKeywordArguments), BodyHandlers.ofString());
			raiseExceptionsIfAny(response);
			return JsonUtils.unmarshall(response.body(), expectedResultType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new A2AClientHTTPError(503, "Network communication error", e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new A2AClientTimeoutError("Client Request timed out.", e);
		}
	}
	
	private <REQ extends JSONRPCRequest<?>, RES extends JSONRPCResponse, T extends StreamingKind> void sendRequest(final REQ requestPayload, final Map<String, Object> httpKeywordArguments, Consumer<T> dataConsumer, Consumer<Throwable> errorConsumer, final Class<RES> expectedResultType) {
		try {
			@SuppressWarnings("unchecked")
			Function<Event, T> mapper = event -> {
				if (event instanceof MessageEvent me) {
					RES response = JsonUtils.unmarshall(me.getData(), expectedResultType);
					if (response instanceof JSONRPCResultResponse<?> result) return (T) result.getResult();
				}
				
				return null;
			};
			
			EventHandler sseEventHandler = new ConsumingEventHandler<T>(mapper, dataConsumer, errorConsumer);
			HttpResponse<Either<Publisher<List<ByteBuffer>>, String>> response = httpClient.send(createHttpPOSTRequest(requestPayload, httpKeywordArguments), new WithErrorBodyHandler<>(BodyHandlers.ofPublisher()));
			var body = response.body();
			if (body.isLeftPresent()) body.getLeft().subscribe(new SSEEventLineSubscriber(sseEventHandler));
			raiseExceptionsIfAny(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new A2AClientHTTPError(503, "Network communication error", e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new A2AClientTimeoutError("Client Request timed out.", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void raiseExceptionsIfAny(final HttpResponse<?> httpResponse) {
		int code = httpResponse.statusCode() / 100;
		if (code == 4 || code == 5) {
			Object body = httpResponse.body();
			String content = null;
			if (body != null) {
				if (body instanceof String s) content = s;
				else if (body instanceof Either either) content = ((Either<Void, String>) either).getRight();
			}
			String contentType = httpResponse.headers().firstValue("content-type").orElse(null);
			if (!Strings.isNullOrEmpty(contentType) && contentType.startsWith("application/json")) {
				throw new A2AClientJSONRPCError(JsonUtils.unmarshall(content, JSONRPCErrorResponse.class));
			}
		}
	}
}
