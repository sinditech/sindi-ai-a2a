/**
 * 
 */
package za.co.sindi.ai.a2a.client.auth;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;
import za.co.sindi.ai.a2a.client.middleware.ClientCallInterceptor;
import za.co.sindi.ai.a2a.types.APIKeySecurityScheme;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.HTTPAuthSecurityScheme;
import za.co.sindi.ai.a2a.types.OAuth2SecurityScheme;
import za.co.sindi.ai.a2a.types.OpenIdConnectSecurityScheme;
import za.co.sindi.ai.a2a.types.SecurityScheme;

/**
 * An interceptor that automatically adds authentication details to requests.
 * 
 * Based on the agent's security schemes.
 * 
 * @author Buhake Sindi
 * @since 31 October 2025
 */
public class AuthInterceptor implements ClientCallInterceptor {
	private static final Logger LOGGER = Logger.getLogger(AuthInterceptor.class.getName());
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
	private static final String BEARER_SCHEME = "Bearer";
	
	private final CredentialService credentialService;

	/**
	 * @param credentialService
	 */
	public AuthInterceptor(CredentialService credentialService) {
		super();
		this.credentialService = Objects.requireNonNull(credentialService, "A credential service is required.");
	}

	/**
	 * Applies authentication headers to the request if credentials are available.
	 */
	@Override
	public RequestPayloadAndKeywordArguments intercept(String methodName, /*Map<String, Object>*/ Object requestPayload,
			Map<String, Object> httpKeywordArguments, AgentCard agentCard, ClientCallContext context) {
		// TODO Auto-generated method stub
		if (agentCard == null || agentCard.getSecurity() == null || agentCard.getSecuritySchemes() == null) return new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
		
		for (Map<String, String[]> requirement : List.of(agentCard.getSecurity())) {
			for (String schemeName : requirement.keySet()) {
				String credential = credentialService.getCredentials(schemeName, context);
				
				if (credential != null && agentCard.getSecuritySchemes().containsKey(schemeName)) {
					SecurityScheme schemeDefUnion = agentCard.getSecuritySchemes().get(schemeName);
					if (schemeDefUnion == null) continue;
					
					@SuppressWarnings("unchecked")
					Map<String, String> headers = (Map<String, String>) httpKeywordArguments.getOrDefault("headers", new ConcurrentHashMap<>());
					
					if (schemeDefUnion instanceof HTTPAuthSecurityScheme httpAuthSecurityScheme) {
						if (httpAuthSecurityScheme.getScheme().toLowerCase().equals(BEARER_SCHEME.toLowerCase())) {
							headers.put(AUTHORIZATION_HEADER_NAME, BEARER_SCHEME + " " + credential);
							LOGGER.fine(String.format("Added Bearer token for scheme '%s' (type: %s).", schemeName, HTTPAuthSecurityScheme.TYPE));
							
							httpKeywordArguments.put("headers", headers);
							return new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
						}
					} 
					
					if (schemeDefUnion instanceof OAuth2SecurityScheme || schemeDefUnion instanceof OpenIdConnectSecurityScheme) {
						headers.put(AUTHORIZATION_HEADER_NAME, BEARER_SCHEME + " " + credential);
						LOGGER.fine(String.format("Added Bearer token for scheme '%s' (type: %s).", schemeName, schemeDefUnion instanceof OAuth2SecurityScheme ? OAuth2SecurityScheme.TYPE : OpenIdConnectSecurityScheme.TYPE));
						
						httpKeywordArguments.put("headers", headers);
						return new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
					} 
					
					if (schemeDefUnion instanceof APIKeySecurityScheme apiKeySecurityScheme) {
						headers.put(apiKeySecurityScheme.getName(), credential);
						LOGGER.fine(String.format("Added API Key Header for scheme '%s' (type: %s).", schemeName, APIKeySecurityScheme.TYPE));
						
						httpKeywordArguments.put("headers", headers);
						return new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
					}
				}
			}
		}
		
		return new RequestPayloadAndKeywordArguments(requestPayload, httpKeywordArguments);
	}
}
