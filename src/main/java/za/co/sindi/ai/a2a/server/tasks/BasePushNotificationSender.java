/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.client.A2AClientJSONRPCError;
import za.co.sindi.ai.a2a.types.JSONRPCErrorResponse;
import za.co.sindi.ai.a2a.types.PushNotificationConfig;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.utils.JsonUtils;
import za.co.sindi.commons.utils.Strings;

/**
 * Base implementation of PushNotificationSender interface.
 * 
 * @author Buhake Sindi
 * @since 09 November 2025
 */
public class BasePushNotificationSender implements PushNotificationSender {
	private static final Logger LOGGER = Logger.getLogger(BasePushNotificationSender.class.getName());
	
	private static final String X_A2A_NOTIFICATION_TOKEN = "X-A2A-Notification-Token";
	
	private final HttpClient httpClient;
	private final PushNotificationConfigStore configStore;
	
	/**
	 * Initializes the BasePushNotificationSender.
	 * 
	 * @param httpClient An async HTTP client instance to send notifications.
	 * @param configStore A {@link PushNotificationConfigStore} instance to retrieve configurations.
	 */
	public BasePushNotificationSender(HttpClient httpClient, PushNotificationConfigStore configStore) {
		super();
		this.httpClient = Objects.requireNonNull(httpClient, "An HTTP client is required.");
		this.configStore = Objects.requireNonNull(configStore, " A PushNotificationConfigStore is required");
	}

	@Override
	public void sendNotification(Task task) {
		// TODO Auto-generated method stub
		List<PushNotificationConfig> pushConfigs = configStore.getInfo(task.getId());
		if (pushConfigs == null || pushConfigs.isEmpty()) return ;
		
		List<CompletableFuture<Boolean>> awaitables = pushConfigs
                .stream()
                .map(pushConfig -> dispatchNotificationAsync(task, pushConfig))
                .toList();
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(awaitables.toArray(new CompletableFuture[0]));
        CompletableFuture<Boolean> allResults = allFutures.thenApply(_ -> awaitables.stream()
                .allMatch(CompletableFuture::join));
		
        try {
			if (!allResults.get()) {
				LOGGER.warning(String.format("Some push notifications failed to send for task_id=%s", task.getId()));
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.WARNING, String.format("Some push notifications failed to send for task_id=%s", task.getId()), e);
		}
	}
	
	private CompletableFuture<Boolean> dispatchNotificationAsync(final Task task, final PushNotificationConfig pushInfo) {
		return CompletableFuture.supplyAsync(() -> dispatchNotification(task, pushInfo));
	}
	
	private boolean dispatchNotification(final Task task, final PushNotificationConfig pushInfo) {
		
		try {
			HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
	                .uri(URI.create(pushInfo.url()))
	                .header("Content-Type", "application/json")
	                .POST(BodyPublishers.ofString(JsonUtils.marshall(task)));
			
			if (!Strings.isNullOrEmpty(pushInfo.token())) {
				httpRequestBuilder.header(X_A2A_NOTIFICATION_TOKEN, pushInfo.token());
			}
			
			HttpResponse<String> response = httpClient.send(httpRequestBuilder.build(), BodyHandlers.ofString());
			raiseExceptionsIfAny(response);
			LOGGER.info(String.format("Push-notification sent for task_id=%s to URL: %s.", task.getId(), pushInfo.url()));
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.severe(String.format("Error sending push-notification for task_id=%s to URL: %s.", task.getId(), pushInfo.url()));
			return false;
		}
		
		return true;
	}
	
	private void raiseExceptionsIfAny(final HttpResponse<String> httpResponse) throws IOException {
		int code = httpResponse.statusCode() / 100;
		if (code == 4 || code == 5) {
			String content = httpResponse.body();
			String contentType = httpResponse.headers().firstValue("content-type").orElse(null);
			if (!Strings.isNullOrEmpty(contentType) && contentType.startsWith("application/json")) {
				throw new A2AClientJSONRPCError(JsonUtils.unmarshall(content, JSONRPCErrorResponse.class));
			} else throw new IOException("HTTP status " + httpResponse.statusCode() + ": " + content);
		}
	}
}
