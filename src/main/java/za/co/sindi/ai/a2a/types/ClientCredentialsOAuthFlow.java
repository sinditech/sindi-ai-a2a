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
public class ClientCredentialsOAuthFlow implements Serializable {

	private String refreshUrl;
	private Map<String, String> scopes;
	private String tokenUrl;

	/**
	 * @param refreshUrl
	 * @param scopes
	 * @param tokenUrl
	 */
	@JsonbCreator
	public ClientCredentialsOAuthFlow(@JsonbProperty("refreshUrl") String refreshUrl, @JsonbProperty("scopes") Map<String, String> scopes, @JsonbProperty("tokenUrl") String tokenUrl) {
		this(tokenUrl, scopes);
		this.refreshUrl = refreshUrl;
	}

	/**
	 * @param tokenUrl
	 * @param scopes
	 */
	public ClientCredentialsOAuthFlow(String tokenUrl, Map<String, String> scopes) {
		super();
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
