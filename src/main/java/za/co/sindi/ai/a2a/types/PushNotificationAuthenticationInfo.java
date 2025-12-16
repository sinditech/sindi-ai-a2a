/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record PushNotificationAuthenticationInfo(String[] schemes, String credentials) implements Serializable {

	public PushNotificationAuthenticationInfo {
		schemes = Objects.requireNonNull(schemes, "An authentication scheme(s) is/are required.");
	}
	
	public PushNotificationAuthenticationInfo(final String[] schemes) {
		this(schemes, null);
	}
	
	@JsonbCreator
	public static PushNotificationAuthenticationInfo create(@JsonbProperty("schemes") String[] schemes, @JsonbProperty("credentials") String credentials) {
		return new PushNotificationAuthenticationInfo(schemes, credentials);
	}
}
