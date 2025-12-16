/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.client.transports.ClientTransport;
import za.co.sindi.ai.a2a.client.transports.JsonRpcTransport;
import za.co.sindi.ai.a2a.client.transports.RestTransport;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.AgentInterface;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.TransportProtocol;
import za.co.sindi.commons.util.Either;

/**
 * @author Buhake Sindi
 * @since 12 December 2025
 */
public class ClientFactory {

	private ClientConfig config;
	private List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers;
	private Map<TransportProtocol, TransportProducer> registry;
	
	/**
	 * @param config
	 */
	public ClientFactory(ClientConfig config) {
		this(config, null);
	}
	
	/**
	 * ClientFactory is used to generate the appropriate client for the agent.
	 * 
     * The factory is configured with a {@link ClientConfig} and optionally a list of
     * Consumer's to use for all generated {@link Client}'s. The expected use is:
	 * 
     * <pre>
     *    var factory = new ClientFactory(config, consumers);
     *    // Then with an agent card make a client with additional consumers and
     *    // interceptors
     *    var client = factory.create(card, additional_consumers, interceptors);
     * </pre>
     * 
     * Now the client can be used consistently regardless of the transport. This
     * aligns the client configuration with the server's capabilities.
     * 
	 * @param config
	 * @param consumers
	 */
	public ClientFactory(ClientConfig config, List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers) {
		super();
		this.config = Objects.requireNonNull(config, "A Client config is required.");
		this.consumers = consumers;
		this.registry = new LinkedHashMap<>();
		registerDefaults(config.supportedTransports());
	}
	
	private void registerDefaults(final List<TransportProtocol> supported) {
		
		if (supported == null || supported.isEmpty() || supported.contains(TransportProtocol.JSONRPC)) {
			register(TransportProtocol.JSONRPC, (card, url, config, interceptors) -> new JsonRpcTransport(config.httpClient() == null ? HttpClient.newHttpClient() : config.httpClient(), url, card, interceptors, config.extensions()) );
		}
		
		if (supported != null && supported.contains(TransportProtocol.HTTP_JSON)) {
			register(TransportProtocol.HTTP_JSON, (card, url, config, interceptors) -> new RestTransport(config.httpClient() == null ? HttpClient.newHttpClient() : config.httpClient(), url, card, interceptors, config.extensions()) );
		}
	}
	
	private void register(final TransportProtocol protocol, final TransportProducer generator) {
		registry.put(protocol, generator);
	}
	
	/**
	 * Create a new {@link Client} for the provided {@link AgentCard}.
	 * 
	 * @param card An {@link AgentCard} defining the characteristics of the agent.
	 * @return A {@link Client} object.
	 */
	public Client create(final AgentCard card) {
		return create(card, null, null, null);
	}
		
	/**
	 * Create a new {@link Client} for the provided {@link AgentCard}.
	 * 
	 * @param card An {@link AgentCard} defining the characteristics of the agent.
	 * @param consumers A list of {@link BiConsumer} methods to pass responses to.
	 * @param interceptors A list of interceptors to use for each request. These
     *       are used for things like attaching credentials or http headers
     *       to all outbound requests.
	 * @param extensions List of extensions to be activated.
	 * @return A {@link Client} object.
	 */
	public Client create(final AgentCard card, final List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> consumers, final List<ClientCallInterceptor> interceptors, final List<String> extensions) {
		Objects.requireNonNull(card, "An Agent card is required.");
		
		Map<TransportProtocol, String> serverSet = new HashMap<>(Map.of(Optional.ofNullable(card.getPreferredTransport()).orElse(TransportProtocol.JSONRPC), card.getUrl()));
		if (card.getAdditionalInterfaces() != null) {
			for (AgentInterface agenttInterface : card.getAdditionalInterfaces()) {
				serverSet.put(agenttInterface.transport(), agenttInterface.url());
			}
		}
		
		Set<TransportProtocol> clientSet = Set.copyOf(Optional.ofNullable(config.supportedTransports()).orElse(List.of(TransportProtocol.JSONRPC)));
		TransportProtocol transportProtocol = null;
		String transportUrl = null;
		
		if (config.useClientPreference()) {
			for (TransportProtocol x : clientSet) {
				if (serverSet.containsKey(x)) {
					transportProtocol = x;
					transportUrl = serverSet.get(x);
					break ;
				}
			}
		} else {
			for (Entry<TransportProtocol, String> x : serverSet.entrySet()) {
				if (clientSet.contains(x.getKey())) {
					transportProtocol = x.getKey();
					transportUrl = x.getValue();
					break ;
				}
			}
		}
		
		if (transportProtocol == null || transportUrl == null || transportUrl.isEmpty()) {
			throw new IllegalStateException("No compatible transports found.");
		}
		
		if (!registry.containsKey(transportProtocol)) {
			throw new IllegalStateException(String.format("No client available for %s.", transportProtocol.toString()));
		}
		
		List<BiConsumer<Either<ClientEvent, Message>, AgentCard>> allConsumers = new ArrayList<>();
		if (this.consumers != null) allConsumers.addAll(this.consumers);
		if (consumers != null && !consumers.isEmpty()) allConsumers.addAll(consumers);
		
		List<String> allExtensions = new ArrayList<>();
		if (this.config.extensions() != null) allExtensions.addAll(this.config.extensions());
		if (extensions != null && !extensions.isEmpty()) allExtensions.addAll(extensions);
		
		ClientTransport transport = registry.get(transportProtocol).get(card, transportUrl, config, interceptors);
		return new BaseClient(card, config, transport, allConsumers, interceptors);
	}
}
