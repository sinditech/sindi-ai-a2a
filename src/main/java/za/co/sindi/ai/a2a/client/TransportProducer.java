/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.util.List;

import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.client.transports.ClientTransport;
import za.co.sindi.ai.a2a.types.AgentCard;

/**
 * @author Buhake Sindi
 * @since 12 December 2025
 */
@FunctionalInterface
public interface TransportProducer {

	public ClientTransport get(final AgentCard agentCard, final String transportUrl, final ClientConfig clientConfig, final List<ClientCallInterceptor> interceptors);
}
