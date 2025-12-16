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
public final class APIKeySecurityScheme extends SecurityScheme {

	public static final String TYPE = "apiKey";
	
	private In in;
	private String name;
	
	/**
	 * @param description
	 * @param in
	 * @param name
	 */
	@JsonbCreator
	public APIKeySecurityScheme(@JsonbProperty("description") String description, @JsonbProperty("in") In in, @JsonbProperty("name") String name) {
		super();
		setDescription(description);
		this.in = in;
		this.name = name;
	}

	/**
	 * @return the in
	 */
	public In getIn() {
		return in;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static enum In {
		JSONRPC("JSONRPC"), // JSON-RPC 2.0 over HTTP (mandatory)
		GRPC("GRPC"), // gRPC over HTTP/2 (optional)
		HTTP_JSON("HTTP+JSON"),
		;
		private final String location;

		/**
		 * @param location
		 */
		private In(String location) {
			this.location = location;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return location;
		}
		
		public static In of(final String value) {
			for (In location : values()) {
				if (location.location.equals(value)) return location;
			}
			
			throw new IllegalArgumentException("Invalid API Key Security scheme location '" + value + "'.");
		}
	}

}
