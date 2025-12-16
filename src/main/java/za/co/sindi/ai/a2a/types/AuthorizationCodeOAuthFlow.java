/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public class AuthorizationCodeOAuthFlow implements Serializable {

	private String authorizationUrl;
	private String refreshUrl;
	private Map<String, String> scopes;
	private String tokenUrl;

	/**
	 * @param authorizationUrl
	 * @param refreshUrl
	 * @param scopes
	 * @param tokenUrl
	 */
	@JsonbCreator
	public AuthorizationCodeOAuthFlow(@JsonbProperty("authorizationUrl") String authorizationUrl, @JsonbProperty("refreshUrl") String refreshUrl, @JsonbProperty("scopes") Map<String, String> scopes, @JsonbProperty("tokenUrl") String tokenUrl) {
		this(authorizationUrl, tokenUrl, scopes);
		this.refreshUrl = refreshUrl;
	}

	/**
	 * @param authorizationUrl
	 * @param tokenUrl
	 * @param scopes
	 */
	public AuthorizationCodeOAuthFlow(String authorizationUrl, String tokenUrl, Map<String, String> scopes) {
		super();
		this.authorizationUrl = Objects.requireNonNull(authorizationUrl);
		this.tokenUrl = Objects.requireNonNull(tokenUrl);
		this.scopes = Objects.requireNonNull(scopes);
	}

	/**
	 * @return the refreshUrl
	 */
	public String getRefreshUrl() {
		return refreshUrl;
	}

	/**
	 * @param refreshUrl the refreshUrl to set
	 */
	public void setRefreshUrl(String refreshUrl) {
		this.refreshUrl = refreshUrl;
	}

	/**
	 * @return the authorizationUrl
	 */
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}

	/**
	 * @return the scopes
	 */
	public Map<String, String> getScopes() {
		return scopes;
	}

	/**
	 * @return the tokenUrl
	 */
	public String getTokenUrl() {
		return tokenUrl;
	}
}
