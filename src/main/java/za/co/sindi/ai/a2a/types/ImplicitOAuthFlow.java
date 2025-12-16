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
public class ImplicitOAuthFlow implements Serializable {

	@JsonbProperty
	private String authorizationUrl;
	
	@JsonbProperty
	private String refreshUrl;
	
	@JsonbProperty
	private Map<String, String> scopes;

	/**
	 * @param authorizationUrl
	 * @param refreshUrl
	 * @param scopes
	 */
	@JsonbCreator
	public ImplicitOAuthFlow(@JsonbProperty("authorizationUrl") String authorizationUrl, @JsonbProperty("refreshUrl") String refreshUrl, @JsonbProperty("scopes") Map<String, String> scopes) {
		this(authorizationUrl, scopes);
		this.refreshUrl = refreshUrl;
	}

	/**
	 * @param authorizationUrl
	 * @param scopes
	 */
	public ImplicitOAuthFlow(String authorizationUrl, Map<String, String> scopes) {
		super();
		this.authorizationUrl = Objects.requireNonNull(authorizationUrl);
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
}
