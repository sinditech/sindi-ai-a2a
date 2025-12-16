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
	public AgentCard(@JsonbProperty("protocolVersion") String protocolVersion, @JsonbProperty("name") String name, @JsonbProperty("description") String description, @JsonbProperty("url") String url,
			@JsonbProperty("preferredTransport") TransportProtocol preferredTransport, @JsonbProperty("additionalInterfaces") AgentInterface[] additionalInterfaces, @JsonbProperty("iconUrl") String iconUrl,
			@JsonbProperty("provider") AgentProvider provider, @JsonbProperty("version") String version, @JsonbProperty("documentUrl") String documentUrl, @JsonbProperty("capabilities") AgentCapabilities capabilities,
			@JsonbProperty("securitySchemes") Map<String, ? extends SecurityScheme> securitySchemes, @JsonbProperty("security") Map<String, String[]>[] security,
			@JsonbProperty("defaultInputModes") String[] defaultInputModes, @JsonbProperty("defaultOutputModes") String[] defaultOutputModes, @JsonbProperty("skills") AgentSkill[] skills,
			@JsonbProperty("supportsAuthenticatedExtendedCard") Boolean supportsAuthenticatedExtendedCard, @JsonbProperty("signatures") AgentCardSignature[] signatures) {
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
	
	public static class AgentCardBuilder {
		private String protocolVersion = "0.3.0";
		private String name;
		private String description;
		private String url;
		private TransportProtocol preferredTransport = TransportProtocol.JSONRPC;
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
		private boolean supportsAuthenticatedExtendedCard = false;
		private AgentCardSignature[] signatures;
		
		/**
		 * @param protocolVersion the protocolVersion to set
		 */
		public AgentCardBuilder protocolVersion(String protocolVersion) {
			this.protocolVersion = protocolVersion;
			return this;
		}

		/**
		 * @param name the name to set
		 */
		public AgentCardBuilder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * @param description the description to set
		 */
		public AgentCardBuilder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * @param url the url to set
		 */
		public AgentCardBuilder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * @param preferredTransport the preferredTransport to set
		 */
		public AgentCardBuilder preferredTransport(TransportProtocol preferredTransport) {
			this.preferredTransport = preferredTransport;
			return this;
		}

		/**
		 * @param additionalInterfaces the additionalInterfaces to set
		 */
		public AgentCardBuilder additionalInterfaces(AgentInterface... additionalInterfaces) {
			this.additionalInterfaces = additionalInterfaces;
			return this;
		}

		/**
		 * @param iconUrl the iconUrl to set
		 */
		public AgentCardBuilder iconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
			return this;
		}

		/**
		 * @param provider the provider to set
		 */
		public AgentCardBuilder provider(AgentProvider provider) {
			this.provider = provider;
			return this;
		}

		/**
		 * @param version the version to set
		 */
		public AgentCardBuilder version(String version) {
			this.version = version;
			return this;
		}

		/**
		 * @param documentUrl the documentUrl to set
		 */
		public AgentCardBuilder documentUrl(String documentUrl) {
			this.documentUrl = documentUrl;
			return this;
		}

		/**
		 * @param capabilities the capabilities to set
		 */
		public AgentCardBuilder capabilities(AgentCapabilities capabilities) {
			this.capabilities = capabilities;
			return this;
		}

		/**
		 * @param securitySchemes the securitySchemes to set
		 */
		public AgentCardBuilder securitySchemes(Map<String, ? extends SecurityScheme> securitySchemes) {
			this.securitySchemes = securitySchemes;
			return this;
		}

		/**
		 * @param security the security to set
		 */
		public AgentCardBuilder security(@SuppressWarnings("unchecked") Map<String, String[]>... security) {
			this.security = security;
			return this;
		}

		/**
		 * @param defaultInputModes the defaultInputModes to set
		 */
		public AgentCardBuilder defaultInputModes(String... defaultInputModes) {
			this.defaultInputModes = defaultInputModes;
			return this;
		}

		/**
		 * @param defaultOutputModes the defaultOutputModes to set
		 */
		public AgentCardBuilder defaultOutputModes(String... defaultOutputModes) {
			this.defaultOutputModes = defaultOutputModes;
			return this;
		}

		/**
		 * @param skills the skills to set
		 */
		public AgentCardBuilder skills(AgentSkill... skills) {
			this.skills = skills;
			return this;
		}

		/**
		 * @param supportsAuthenticatedExtendedCard the supportsAuthenticatedExtendedCard to set
		 */
		public AgentCardBuilder supportsAuthenticatedExtendedCard(boolean supportsAuthenticatedExtendedCard) {
			this.supportsAuthenticatedExtendedCard = supportsAuthenticatedExtendedCard;
			return this;
		}

		/**
		 * @param signatures the signatures to set
		 */
		public AgentCardBuilder signatures(AgentCardSignature... signatures) {
			this.signatures = signatures;
			return this;
		}

		public AgentCard build() {
			return new AgentCard(protocolVersion, name, description, url, preferredTransport, additionalInterfaces, iconUrl, provider, version, documentUrl, capabilities, securitySchemes, security, defaultInputModes, defaultOutputModes, skills, supportsAuthenticatedExtendedCard, signatures);
		}
	}
}
