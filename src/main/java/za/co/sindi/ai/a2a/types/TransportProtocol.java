/**
 * 
 */
package za.co.sindi.ai.a2a.types;

/**
 * @author Buhake Sindi
 * @since 14 October 2025
 */
public enum TransportProtocol {
	JSONRPC("JSONRPC"), // JSON-RPC 2.0 over HTTP (mandatory)
	GRPC("GRPC"), // gRPC over HTTP/2 (optional)
	HTTP_JSON("HTTP+JSON"),
	;
	private final String protocol;

	/**
	 * @param protocol
	 */
	private TransportProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return protocol;
	}
	
	public static TransportProtocol of(final String value) {
		for (TransportProtocol protocol : values()) {
			if (protocol.protocol.equals(value)) return protocol;
		}
		
		throw new IllegalArgumentException("Invalid transport protocol value '" + value + "'.");
	}
}
