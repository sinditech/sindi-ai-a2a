/**
 * 
 */
package za.co.sindi.ai.a2a.client.auth;

import za.co.sindi.ai.a2a.client.middleware.ClientCallContext;

/**
 * An abstract service for retrieving credentials.
 * 
 * @author Buhake Sindi
 * @since 31 October 2025
 */
public interface CredentialService {

	/**
	 * Retrieves a credential (e.g., token) for a security scheme.
	 * 
	 * @param securitySchemeName The name of the security scheme.
	 * @param context The client call context.
	 * @return The credential string, or <code>null</code> if not found.
	 */
	public String getCredentials(final String securitySchemeName, final ClientCallContext context);
}
