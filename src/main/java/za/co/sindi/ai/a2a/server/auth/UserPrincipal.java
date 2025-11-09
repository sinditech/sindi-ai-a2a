/**
 * 
 */
package za.co.sindi.ai.a2a.server.auth;

import java.security.Principal;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public interface UserPrincipal extends Principal {

	public boolean isAuthenticated();
}
