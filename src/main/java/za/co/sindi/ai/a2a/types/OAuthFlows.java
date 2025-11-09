/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public class OAuthFlows implements Serializable {

	@JsonbProperty
	private AuthorizationCodeOAuthFlow authorizationCode;
	
	@JsonbProperty
	private ClientCredentialsOAuthFlow clientCredentials;
	
	@JsonbProperty
	private ImplicitOAuthFlow implicit;
	
	@JsonbProperty
	private PasswordOAuthFlow password;

	/**
	 * @return the authorizationCode
	 */
	public AuthorizationCodeOAuthFlow getAuthorizationCode() {
		return authorizationCode;
	}

	/**
	 * @param authorizationCode the authorizationCode to set
	 */
	public void setAuthorizationCode(AuthorizationCodeOAuthFlow authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	/**
	 * @return the clientCredentials
	 */
	public ClientCredentialsOAuthFlow getClientCredentials() {
		return clientCredentials;
	}

	/**
	 * @param clientCredentials the clientCredentials to set
	 */
	public void setClientCredentials(ClientCredentialsOAuthFlow clientCredentials) {
		this.clientCredentials = clientCredentials;
	}

	/**
	 * @return the implicit
	 */
	public ImplicitOAuthFlow getImplicit() {
		return implicit;
	}

	/**
	 * @param implicit the implicit to set
	 */
	public void setImplicit(ImplicitOAuthFlow implicit) {
		this.implicit = implicit;
	}

	/**
	 * @return the password
	 */
	public PasswordOAuthFlow getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(PasswordOAuthFlow password) {
		this.password = password;
	}
}
