/**
 * 
 */
package za.co.sindi.ai.a2a.client.auth;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;

/**
 * A simple in-memory store for session-keyed credentials.
 * 
 * This class uses the 'sessionId' from the ClientCallContext state to
 * store and retrieve credentials...
 *    
 * @author Buhake Sindi
 * @since 31 October 2025
 */
public class InMemoryContextCredentialStore implements CredentialService {
	
	private static final String SESSION_ID = "sessionId";
	
	private final ConcurrentMap<String, ConcurrentMap<String, String>> store;

	/**
	 * 
	 */
	public InMemoryContextCredentialStore() {
		super();
		// TODO Auto-generated constructor stub
		this.store = new ConcurrentHashMap<>();
	}

	@Override
	public String getCredentials(String securitySchemeName, ClientCallContext context) {
		// TODO Auto-generated method stub
		if (context == null || context.state() == null || !context.state().containsKey(SESSION_ID)) return null;
		Object sessionIdObject = context.state().containsKey(SESSION_ID);
		if (!(sessionIdObject instanceof String sessionId)) return null;
		return store.getOrDefault(sessionId, new ConcurrentHashMap<>()).get(securitySchemeName);
	}
	
	/**
	 * Method to populate the store.
	 * @param sessionId The session ID
	 * @param securitySchemeName The name of the security scheme.
	 * @param credential The credential string
	 */
	public void setCredentials(final String sessionId, final String securitySchemeName, final String credential) {
		store.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>()).put(securitySchemeName, credential);
	}
}
