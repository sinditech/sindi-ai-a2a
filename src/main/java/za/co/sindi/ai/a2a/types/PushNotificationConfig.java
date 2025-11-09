/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public record PushNotificationConfig(@JsonbProperty String id, @JsonbProperty String url, @JsonbProperty String token, @JsonbProperty PushNotificationAuthenticationInfo authentication) {

	public PushNotificationConfig {
		url = Objects.requireNonNull(url, "The callback URL where the agent should send push notifications is required.");
	}
	
	public PushNotificationConfig(final String url) {
		this(null, url, null, null);
	}
	
	public static final class Builder {
		private String id;
		private String url;
		private String token;
		private PushNotificationAuthenticationInfo authentication;
	
		public Builder() {}
		
		public Builder(PushNotificationConfig config) {
			id(config.id()).
			url(config.url()).
			token(config.token()).
			authentication(config.authentication());
		}

		/**
		 * @param id the id to set
		 */
		public Builder id(String id) {
			this.id = id;
			return this;
		}

		/**
		 * @param url the url to set
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * @param token the token to set
		 */
		public Builder token(String token) {
			this.token = token;
			return this;
		}

		/**
		 * @param authentication the authentication to set
		 */
		public Builder authentication(PushNotificationAuthenticationInfo authentication) {
			this.authentication = authentication;
			return this;
		}
		
		public PushNotificationConfig build() {
			return new PushNotificationConfig(id, url, token, authentication);
		}
	}
}
