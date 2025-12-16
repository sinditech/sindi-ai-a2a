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
public final class HTTPAuthSecurityScheme extends SecurityScheme {
	
	public static final String TYPE = "http";
	
	private String bearerFormat;
	private String scheme;

	/**
	 * @param bearerFormat
	 * @param description
	 * @param scheme
	 */
	@JsonbCreator
	public HTTPAuthSecurityScheme(@JsonbProperty("bearerFormat") String bearerFormat, @JsonbProperty("description") String description, @JsonbProperty("scheme") String scheme) {
		super();
		this.bearerFormat = bearerFormat;
		setDescription(description);
		this.scheme = scheme;
	}

	/**
	 * @param scheme
	 */
	public HTTPAuthSecurityScheme(String scheme) {
		super();
		this.scheme = Objects.requireNonNull(scheme);
	}

	/**
	 * @return the bearerFormat
	 */
	public String getBearerFormat() {
		return bearerFormat;
	}

	/**
	 * @param bearerFormat the bearerFormat to set
	 */
	public void setBearerFormat(String bearerFormat) {
		this.bearerFormat = bearerFormat;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}
}
