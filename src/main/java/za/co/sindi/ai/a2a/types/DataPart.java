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
public final class DataPart extends PartBase {

	private Map<String, Object> data;

	/**
	 * @param data
	 */
	public DataPart(Map<String, Object> data) {
		super();
		this.data = Objects.requireNonNull(data, "Data is required.");
	}
	
	/**
	 * @param data
	 * @param metadata
	 */
	@JsonbCreator
	public DataPart(@JsonbProperty("data") Map<String, Object> data, @JsonbProperty("metadata") Map<String, Object> metadata) {
		this(data);
		setMetadata(metadata);
	}

	/**
	 * @return the data
	 */
	public Map<String, Object> getData() {
		return data;
	}
}
