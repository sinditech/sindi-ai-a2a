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
 * @since 21 October 2025
 */
public record Artifact(String artifactId, String name, String description, Part[] parts, Map<String, Object> metadata, String[] extensions) implements Serializable {

	public Artifact {
		artifactId = Objects.requireNonNull(artifactId, "Artifact ID is required.");
	}
	
	public Artifact(String artifactId) {
		this(artifactId, null, null, null, null, null);
	}
	
	@JsonbCreator
	public static Artifact create(@JsonbProperty("artifactId") String artifactId, @JsonbProperty("name") String name, @JsonbProperty("description") String description, @JsonbProperty("parts") Part[] parts, @JsonbProperty("metadata") Map<String, Object> metadata, @JsonbProperty("extensions") String[] extensions) {
		return new Artifact(artifactId, name, description, parts, metadata, extensions);
	}
	
	public static final class Builder {
		private String artifactId;
		private String name;
		private String description;
		private Part[] parts;
		private Map<String, Object> metadata;
		private String[] extensions;
		
		public Builder() {}
		
		public Builder(final Artifact artifact) {
			artifactId(artifact.artifactId()).
			name(artifact.name()).
			description(artifact.description()).
			parts(artifact.parts()).
			metadata(artifact.metadata()).
			extensions(artifact.extensions());
		}

		/**
		 * @param artifactId the artifactId to set
		 */
		public Builder artifactId(String artifactId) {
			this.artifactId = artifactId;
			return this;
		}

		/**
		 * @param name the name to set
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * @param description the description to set
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * @param parts the parts to set
		 */
		public Builder parts(Part[] parts) {
			this.parts = parts;
			return this;
		}

		/**
		 * @param metadata the metadata to set
		 */
		public Builder metadata(Map<String, Object> metadata) {
			this.metadata = metadata;
			return this;
		}

		/**
		 * @param extensions the extensions to set
		 */
		public Builder extensions(String[] extensions) {
			this.extensions = extensions;
			return this;
		}
		
		public Artifact build() {
			return Artifact.create(artifactId, name, description, parts, metadata, extensions);
		}
	}
}
