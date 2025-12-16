/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import jakarta.json.bind.JsonbException;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.utils.JsonUtils;

/**
 * @author Buhake Sindi
 * @since 02 November 2025
 */
public class A2ACardResolver {
	private static final Logger LOGGER = Logger.getLogger(A2ACardResolver.class.getName());
	private static final String AGENT_CARD_WELL_KNOWN_PATH = "/.well-known/agent-card.json";

	private final HttpClient httpClient;
	private final String baseUrl;
	private final String agentCardPath;
	
	/**
	 * @param httpClient
	 * @param baseUrl
	 */
	public A2ACardResolver(HttpClient httpClient, String baseUrl) {
		this(httpClient, baseUrl, AGENT_CARD_WELL_KNOWN_PATH);
	}
	
	/**
	 * @param httpClient
	 * @param baseUrl
	 * @param agentCardPath
	 */
	public A2ACardResolver(HttpClient httpClient, String baseUrl, String agentCardPath) {
		super();
		this.httpClient = Objects.requireNonNull(httpClient, "A HTTP client is required.");
		this.baseUrl = Objects.requireNonNull(baseUrl, "A base url is required.").replaceAll("/+$", "");
		this.agentCardPath = Objects.requireNonNull(agentCardPath, "An HTTP agent card path is required.").replaceAll("^/+", "");
	}
	
	/**
	 * Fetches an agent card from a specified path relative to the base_url.
	 * 
	 * @return An {@link AgentCard} object representing the agent's capabilities.
	 * @throws A2AClientHTTPError If an HTTP error occurs during the request.
	 * @throws A2AClientJSONError If the response body cannot be decoded as JSON or validated against the {@link AgentCard} schema.
	 */
	public AgentCard getAgentCard() {
		return getAgentCard(null, null);
	}
	
	/**
	 * Fetches an agent card from a specified path relative to the base_url.
	 * 
	 * @return An {@link AgentCard} object representing the agent's capabilities.
	 * @throws A2AClientHTTPError If an HTTP error occurs during the request.
	 * @throws A2AClientJSONError If the response body cannot be decoded as JSON or validated against the {@link AgentCard} schema.
	 */
	public AgentCard getAgentCard(final String relativeCardPath, final Map<String, Object> httpKeywordArguments) {
		String pathSegment = relativeCardPath == null ? agentCardPath : relativeCardPath.replaceAll("/+$", "");
		String targetUrl = baseUrl + "/" + pathSegment;
		
		HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
						                .uri(URI.create(targetUrl))
						                .header("Content-Type", "application/json")
						                .GET();
		
		@SuppressWarnings("unchecked")
		Map<String, String> httpHeaders = httpKeywordArguments != null ? (Map<String, String>) httpKeywordArguments.get("headers") : null;
		if (httpHeaders != null) {
			for (Entry<String, String> header : httpHeaders.entrySet()) {
				httpRequestBuilder.header(header.getKey(), header.getValue());
			}
		}
		
		if (httpKeywordArguments != null && httpKeywordArguments.containsKey("timeout")) {
			httpRequestBuilder.timeout((Duration) httpKeywordArguments.get("timeout"));
		}
		
		try {
			HttpResponse<String> response = httpClient.send(httpRequestBuilder.build(), BodyHandlers.ofString());
			AgentCard agentCard = JsonUtils.unmarshall(response.body(), AgentCard.class);
			LOGGER.info(String.format("Successfully fetched agent card data from %s: %s", targetUrl, agentCard.getName()));
			return agentCard;
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			throw new A2AClientHTTPError(503, "Network communication error fetching agent card from " + targetUrl + ":", e);
		} catch (JsonbException e) {
			// TODO Auto-generated catch block
			throw new A2AClientJSONError("Failed to parse JSON for agent card from " + targetUrl + ":", e);
		}
	}
}
