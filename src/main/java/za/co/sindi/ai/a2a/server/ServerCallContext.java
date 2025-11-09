/**
 * 
 */
package za.co.sindi.ai.a2a.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import za.co.sindi.ai.a2a.server.auth.UnauthenticatedUserPrincipal;
import za.co.sindi.ai.a2a.server.auth.UserPrincipal;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public class ServerCallContext {

	private final ConcurrentMap<Object, Object> modelConfig = new ConcurrentHashMap<>();
	private final Map<String, Object> state;
	private final UserPrincipal user;
	private final Set<String> requestedExtensions;
	private final Set<String> activatedExtensions;
	
	/**
	 * 
	 */
	public ServerCallContext() {
		this(new ConcurrentHashMap<>(), new UnauthenticatedUserPrincipal(), null, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param state
	 * @param user
	 * @param requestedExtensions
	 * @param activatedExtensions
	 */
	public ServerCallContext(Map<String, Object> state, UserPrincipal user, Set<String> requestedExtensions,
			Set<String> activatedExtensions) {
		super();
		this.state = state;
		this.user = user;
		this.requestedExtensions = requestedExtensions == null ? new HashSet<>() : new HashSet<>(requestedExtensions);
		this.activatedExtensions = activatedExtensions == null ? new HashSet<>() : new HashSet<>(activatedExtensions);
	}

	/**
	 * @return the modelConfig
	 */
	public ConcurrentMap<Object, Object> getModelConfig() {
		return modelConfig;
	}

	/**
	 * @return the state
	 */
	public Map<String, Object> getState() {
		return state;
	}

	/**
	 * @return the user
	 */
	public UserPrincipal getUser() {
		return user;
	}

	/**
	 * @return the requestedExtensions
	 */
	public Set<String> getRequestedExtensions() {
		return requestedExtensions;
	}

	/**
	 * @return the activatedExtensions
	 */
	public Set<String> getActivatedExtensions() {
		return activatedExtensions;
	}
}
