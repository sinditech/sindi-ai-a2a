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
public class AgentSkill implements Serializable {

	private String id;
	private String name;
	private String description;
	private String[] tags;
	private String[] examples;
	private String[] inputModes;
	private String[] outputModes;
	private Map<String, String[]> security;

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param tags
	 * @param examples
	 * @param inputModes
	 * @param outputModes
	 * @param security
	 */
	@JsonbCreator
	public AgentSkill(@JsonbProperty("id") String id, @JsonbProperty("name") String name, @JsonbProperty("description") String description, @JsonbProperty("tags") String[] tags, @JsonbProperty("examples") String[] examples, @JsonbProperty("inputModes") String[] inputModes,
			@JsonbProperty("outputModes") String[] outputModes, @JsonbProperty("security") Map<String, String[]> security) {
		this(id, name, description, tags);
		this.examples = examples;
		this.inputModes = inputModes;
		this.outputModes = outputModes;
		this.security = security;
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param tags
	 */
	public AgentSkill(String id, String name, String description, String[] tags) {
		super();
		this.id = Objects.requireNonNull(id, "A unique identifier for the agent's skill is required.");
		this.name = Objects.requireNonNull(name, "A human-readable name for the skill is required.");
		this.description = Objects.requireNonNull(description, "A detailed description of the skill is required.");
		this.tags = Objects.requireNonNull(tags, "A set of keywords describing the skill's capabilities are required.");
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * @return the examples
	 */
	public String[] getExamples() {
		return examples;
	}

	/**
	 * @param examples the examples to set
	 */
	public void setExamples(String[] examples) {
		this.examples = examples;
	}

	/**
	 * @return the inputModes
	 */
	public String[] getInputModes() {
		return inputModes;
	}

	/**
	 * @param inputModes the inputModes to set
	 */
	public void setInputModes(String[] inputModes) {
		this.inputModes = inputModes;
	}

	/**
	 * @return the outputModes
	 */
	public String[] getOutputModes() {
		return outputModes;
	}

	/**
	 * @param outputModes the outputModes to set
	 */
	public void setOutputModes(String[] outputModes) {
		this.outputModes = outputModes;
	}

	/**
	 * @return the security
	 */
	public Map<String, String[]> getSecurity() {
		return security;
	}

	/**
	 * @param security the security to set
	 */
	public void setSecurity(Map<String, String[]> security) {
		this.security = security;
	}
}
