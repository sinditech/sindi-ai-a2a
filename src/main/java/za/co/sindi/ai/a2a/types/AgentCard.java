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
public class AgentCard implements Serializable {

	private String protocolVersion;
	private String name;
	private String description;
	private String url;
	private TransportProtocol preferredTransport;
	private AgentInterface[] additionalInterfaces;
	private String iconUrl;
	private AgentProvider provider;
	private String version;
	private String documentUrl;
	private AgentCapabilities capabilities;
	private Map<String, ? extends SecurityScheme> securitySchemes;
	private Map<String, String[]>[] security;
	private String[] defaultInputModes;
	private String[] defaultOutputModes;
	private AgentSkill[] skills;
	private Boolean supportsAuthenticatedExtendedCard;
	private AgentCardSignature[] signatures;

	/**
	 * @param protocolVersion
	 * @param name
	 * @param description
	 * @param url
	 * @param preferredTransport
	 * @param additionalInterfaces
	 * @param iconUrl
	 * @param provider
	 * @param version
	 * @param documentUrl
	 * @param capabilities
	 * @param securitySchemes
	 * @param security
	 * @param defaultInputModes
	 * @param defaultOutputModes
	 * @param skills
	 * @param supportsAuthenticatedExtendedCard
	 * @param signatures
	 */
	@JsonbCreator
	public AgentCard(@JsonbProperty String protocolVersion, @JsonbProperty String name, @JsonbProperty String description, @JsonbProperty String url,
			@JsonbProperty TransportProtocol preferredTransport, @JsonbProperty AgentInterface[] additionalInterfaces, @JsonbProperty String iconUrl,
			@JsonbProperty AgentProvider provider, @JsonbProperty String version, @JsonbProperty String documentUrl, @JsonbProperty AgentCapabilities capabilities,
			@JsonbProperty Map<String, ? extends SecurityScheme> securitySchemes, @JsonbProperty Map<String, String[]>[] security,
			@JsonbProperty String[] defaultInputModes, @JsonbProperty String[] defaultOutputModes, @JsonbProperty AgentSkill[] skills,
			@JsonbProperty Boolean supportsAuthenticatedExtendedCard, @JsonbProperty AgentCardSignature[] signatures) {
		this(protocolVersion, name, description, url, version, capabilities, defaultInputModes, defaultOutputModes, skills);
		this.preferredTransport = preferredTransport;
		this.additionalInterfaces = additionalInterfaces;
		this.iconUrl = iconUrl;
		this.provider = provider;
		this.documentUrl = documentUrl;
		this.securitySchemes = securitySchemes;
		this.security = security;
		this.supportsAuthenticatedExtendedCard = supportsAuthenticatedExtendedCard;
		this.signatures = signatures;
	}

	/**
	 * @param protocolVersion
	 * @param name
	 * @param description
	 * @param url
	 * @param version
	 * @param capabilities
	 * @param defaultInputModes
	 * @param defaultOutputModes
	 * @param skills
	 */
	public AgentCard(String protocolVersion, String name, String description, String url, String version,
			AgentCapabilities capabilities, String[] defaultInputModes, String[] defaultOutputModes, AgentSkill[] skills) {
		super();
		this.protocolVersion = Objects.requireNonNull(protocolVersion);
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
		this.url = Objects.requireNonNull(url);
		this.version = Objects.requireNonNull(version);
		this.capabilities = Objects.requireNonNull(capabilities);
		this.defaultInputModes = Objects.requireNonNull(defaultInputModes);
		this.defaultOutputModes = Objects.requireNonNull(defaultOutputModes);
		this.skills = Objects.requireNonNull(skills);
	}

	/**
	 * @return the protocolVersion
	 */
	public String getProtocolVersion() {
		return protocolVersion;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return the capabilities
	 */
	public AgentCapabilities getCapabilities() {
		return capabilities;
	}

	/**
	 * @return the defaultInputModes
	 */
	public String[] getDefaultInputModes() {
		return defaultInputModes;
	}

	/**
	 * @return the defaultOutputModes
	 */
	public String[] getDefaultOutputModes() {
		return defaultOutputModes;
	}

	/**
	 * @return the skills
	 */
	public AgentSkill[] getSkills() {
		return skills;
	}

	/**
	 * @return the preferredTransport
	 */
	public TransportProtocol getPreferredTransport() {
		return preferredTransport;
	}

	/**
	 * @param preferredTransport the preferredTransport to set
	 */
	public void setPreferredTransport(TransportProtocol preferredTransport) {
		this.preferredTransport = preferredTransport;
	}

	/**
	 * @return the additionalInterfaces
	 */
	public AgentInterface[] getAdditionalInterfaces() {
		return additionalInterfaces;
	}

	/**
	 * @param additionalInterfaces the additionalInterfaces to set
	 */
	public void setAdditionalInterfaces(AgentInterface[] additionalInterfaces) {
		this.additionalInterfaces = additionalInterfaces;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the provider
	 */
	public AgentProvider getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(AgentProvider provider) {
		this.provider = provider;
	}

	/**
	 * @return the documentUrl
	 */
	public String getDocumentUrl() {
		return documentUrl;
	}

	/**
	 * @param documentUrl the documentUrl to set
	 */
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	/**
	 * @return the securitySchemes
	 */
	public Map<String, ? extends SecurityScheme> getSecuritySchemes() {
		return securitySchemes;
	}

	/**
	 * @param securitySchemes the securitySchemes to set
	 */
	public void setSecuritySchemes(Map<String, ? extends SecurityScheme> securitySchemes) {
		this.securitySchemes = securitySchemes;
	}

	/**
	 * @return the security
	 */
	public Map<String, String[]>[] getSecurity() {
		return security;
	}

	/**
	 * @param security the security to set
	 */
	public void setSecurity(Map<String, String[]>[] security) {
		this.security = security;
	}

	/**
	 * @return the supportsAuthenticatedExtendedCard
	 */
	public Boolean getSupportsAuthenticatedExtendedCard() {
		return supportsAuthenticatedExtendedCard;
	}

	/**
	 * @param supportsAuthenticatedExtendedCard the supportsAuthenticatedExtendedCard to set
	 */
	public void setSupportsAuthenticatedExtendedCard(Boolean supportsAuthenticatedExtendedCard) {
		this.supportsAuthenticatedExtendedCard = supportsAuthenticatedExtendedCard;
	}

	/**
	 * @return the signatures
	 */
	public AgentCardSignature[] getSignatures() {
		return signatures;
	}

	/**
	 * @param signatures the signatures to set
	 */
	public void setSignatures(AgentCardSignature[] signatures) {
		this.signatures = signatures;
	}
}
