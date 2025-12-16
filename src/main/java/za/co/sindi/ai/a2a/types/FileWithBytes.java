/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public final class FileWithBytes extends FileBase {

	private String bytes;

	/**
	 * @param bytes
	 */
	public FileWithBytes(String bytes) {
		super();
		this.bytes = Objects.requireNonNull(bytes, "The base64-encoded content bytes are required.");
	}
	
	/**
	 * 
	 * @param bytes
	 * @param name
	 * @param mimeTye
	 */
	@JsonbCreator
	public FileWithBytes(@JsonbProperty("bytes") String bytes, @JsonbProperty("name") String name, @JsonbProperty("mimeType") String mimeTye) {
		this(bytes);
		setName(name);
		setMimeType(mimeTye);
	}

	/**
	 * @return the bytes
	 */
	public String getBytes() {
		return bytes;
	}
}
