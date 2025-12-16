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
public final class MutualTLSSecurityScheme extends SecurityScheme {

	public static final String TYPE = "mutualTLS";
	
	/**
	 * 
	 */
	public MutualTLSSecurityScheme() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param description
	 */
	@JsonbCreator
	public MutualTLSSecurityScheme(@JsonbProperty("description") String description) {
		this();
		// TODO Auto-generated constructor stub
		setDescription(description);
	}
}
