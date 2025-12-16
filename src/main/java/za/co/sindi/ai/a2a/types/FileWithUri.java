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
public final class FileWithUri extends FileBase {

	private String uri;
	
	/**
	 * @param uri
	 */
	public FileWithUri(String uri) {
		super();
		this.uri = Objects.requireNonNull(uri, "The base64-encoded content bytes are required.");
	}
	
	/**
	 * 
	 * @param uri
	 * @param name
	 * @param mimeTye
	 */
	@JsonbCreator
	public FileWithUri(@JsonbProperty("uri") String uri, @JsonbProperty("name") String name, @JsonbProperty("mimeType") String mimeTye) {
		this(uri);
		setName(name);
		setMimeType(mimeTye);
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
}
