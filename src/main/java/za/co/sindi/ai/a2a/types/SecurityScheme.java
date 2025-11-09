/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
@JsonbTypeInfo(
	key = "type",
	value = {
	    @JsonbSubtype(alias=APIKeySecurityScheme.TYPE, type=APIKeySecurityScheme.class),
	    @JsonbSubtype(alias=HTTPAuthSecurityScheme.TYPE, type=HTTPAuthSecurityScheme.class),
	    @JsonbSubtype(alias=OAuth2SecurityScheme.TYPE, type=OAuth2SecurityScheme.class),
	    @JsonbSubtype(alias=OpenIdConnectSecurityScheme.TYPE, type=OpenIdConnectSecurityScheme.class),
	    @JsonbSubtype(alias=MutualTLSSecurityScheme.TYPE, type=MutualTLSSecurityScheme.class),
	}
)
public sealed abstract class SecurityScheme permits APIKeySecurityScheme, HTTPAuthSecurityScheme, OAuth2SecurityScheme, OpenIdConnectSecurityScheme, MutualTLSSecurityScheme {

	private String description;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
