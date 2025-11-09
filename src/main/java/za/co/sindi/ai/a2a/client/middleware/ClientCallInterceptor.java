/**
 * 
 */
package za.co.sindi.ai.a2a.client.middleware;

import java.util.Map;

import za.co.sindi.ai.a2a.types.AgentCard;

/**
 * An abstract base class for client-side call interceptors.
 *
 * Interceptors can inspect and modify requests before they are sent,
 * which is ideal for concerns like authentication, logging, or tracing.
 * 
 * @author Buhake Sindi
 * @since 31 October 2025
 */
public interface ClientCallInterceptor {

	/**
	 * Intercepts a client call before the request is sent.
	 * 
	 * @param methodName The name of the RPC method (e.g., "message/send").
	 * @param requestPayload The JSON RPC request payload dictionary.
	 * @param keywordArguments The keyword arguments for the httpx request.
	 * @param agentCard The {@link AgentCard} associated with the client.
	 * @param context The {@link ClientCallContext} for this specific call.
	 * @return A tuple containing the (potentially modified) request_payload and http keyword arguments.
	 */
	public RequestPayloadAndKeywordArguments intercept(final String methodName, final /*Map<String, Object>*/ Object requestPayload, final Map<String, Object> keywordArguments, final AgentCard agentCard, final ClientCallContext context);
	
	public static record RequestPayloadAndKeywordArguments(/*Map<String, Object>*/ Object payload, Map<String, Object> keywordArguments) {}
}
