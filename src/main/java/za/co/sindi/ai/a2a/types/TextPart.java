/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public final class TextPart extends PartBase {

	private String text;

	/**
	 * @param text
	 */
	public TextPart(String text) {
		super();
		this.text = Objects.requireNonNull(text, "Text is required.");
	}
	
	/**
	 * @param text
	 * @param metadata
	 */
	@JsonbCreator
	public TextPart(@JsonbProperty("text") String text, @JsonbProperty("metadata") Map<String, Object> metadata) {
		this(text);
		setMetadata(metadata);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
