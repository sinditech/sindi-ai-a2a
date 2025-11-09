/**
 * 
 */
package za.co.sindi.ai.a2a.server.auth;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class UnauthenticatedUserPrincipal implements UserPrincipal {

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "";
	}
}
