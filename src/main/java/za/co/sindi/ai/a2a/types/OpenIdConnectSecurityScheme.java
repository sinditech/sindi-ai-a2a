/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public final class OpenIdConnectSecurityScheme extends SecurityScheme {
	
	public static final String TYPE = "openIdConnect";
	
	private String openIdConnectUrl;

	/**
	 * @param description
	 * @param openIdConnectUrl
	 */
	@JsonbCreator
	public OpenIdConnectSecurityScheme(@JsonbProperty("description") String description, @JsonbProperty("openIdConnectUrl") String openIdConnectUrl) {
		this(openIdConnectUrl);
		setDescription(description);
	}
	
	/**
	 * @param openIdConnectUrl
	 */
	public OpenIdConnectSecurityScheme(final String openIdConnectUrl) {
		super();
		this.openIdConnectUrl = openIdConnectUrl;
	}

	/**
	 * @return the openIdConnectUrl
	 */
	public String getOpenIdConnectUrl() {
		return openIdConnectUrl;
	}
}
