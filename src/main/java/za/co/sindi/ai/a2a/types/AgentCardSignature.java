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
public class AgentCardSignature implements Serializable {

	private String protectedHeader;
	private String signature;
	private Map<String, Object> header;

	/**
	 * @param protectedHeader
	 * @param signature
	 * @param header
	 */
	@JsonbCreator
	public AgentCardSignature(@JsonbProperty("protected") String protectedHeader, @JsonbProperty("signature") String signature, @JsonbProperty("header") Map<String, Object> header) {
		this(protectedHeader, signature);
		this.header = header;
	}

	/**
	 * @param protectedHeader
	 * @param signature
	 */
	public AgentCardSignature(String protectedHeader, String signature) {
		super();
		this.protectedHeader = Objects.requireNonNull(protectedHeader, "The Base64url-encoded protected JWS header for the signature is required.");
		this.signature = Objects.requireNonNull(signature, "The computed Base64url-encoded signature is required.");;
	}

	/**
	 * @return the protectedHeader
	 */
	public String getProtectedHeader() {
		return protectedHeader;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @return the header
	 */
	public Map<String, Object> getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Map<String, Object> header) {
		this.header = header;
	}
}
