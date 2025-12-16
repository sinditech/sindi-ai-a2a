/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public final class OAuth2SecurityScheme extends SecurityScheme {
	
	public static final String TYPE = "oauth2";
	
	private OAuthFlows flows;
	private String oauth2MetadataUrl;

	/**
	 * @param description
	 * @param flows
	 * @param oauth2MetadataUrl
	 */
	@JsonbCreator
	public OAuth2SecurityScheme(@JsonbProperty("description") String description, @JsonbProperty("flows") OAuthFlows flows, @JsonbProperty("oauth2MetadataUrl") String oauth2MetadataUrl) {
		this(flows);
		setDescription(description);
		this.oauth2MetadataUrl = oauth2MetadataUrl;
	}

	/**
	 * @param flows
	 */
	public OAuth2SecurityScheme(OAuthFlows flows) {
		super();
		this.flows = Objects.requireNonNull(flows);
	}

	/**
	 * @return the oauth2MetadataUrl
	 */
	public String getOauth2MetadataUrl() {
		return oauth2MetadataUrl;
	}

	/**
	 * @param oauth2MetadataUrl the oauth2MetadataUrl to set
	 */
	public void setOauth2MetadataUrl(String oauth2MetadataUrl) {
		this.oauth2MetadataUrl = oauth2MetadataUrl;
	}

	/**
	 * @return the flows
	 */
	public OAuthFlows getFlows() {
		return flows;
	}
}
