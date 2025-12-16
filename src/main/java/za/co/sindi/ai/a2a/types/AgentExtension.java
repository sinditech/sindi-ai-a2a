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
public class AgentExtension implements Serializable {

	private String uri;
	private String description;
	private Boolean required;
	
	@JsonbProperty("params") 
	private Map<String, Object> parameters;

	/**
	 * @param uri
	 * @param description
	 * @param required
	 * @param parameters
	 */
	@JsonbCreator
	public AgentExtension(@JsonbProperty("uri") String uri, @JsonbProperty("description") String description, @JsonbProperty("required") Boolean required, @JsonbProperty("params") Map<String, Object> parameters) {
		this(uri);
		this.description = description;
		this.required = required;
		this.parameters = parameters;
	}

	/**
	 * @param uri
	 */
	public AgentExtension(String uri) {
		super();
		this.uri = Objects.requireNonNull(uri, "The unique URI identifying the extension is required.");
	}

	/**
	 * @param uri
	 * @param description
	 */
	public AgentExtension(String uri, String description) {
		this(uri);
		this.description = description;
	}

	/**
	 * @param uri
	 * @param description
	 * @param required
	 */
	public AgentExtension(String uri, String description, boolean required) {
		this(uri, description);
		this.required = required;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
