/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.DataPart;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.TextPart;

/**
 * @author Buhake Sindi
 * @since 01 April 2026
 */
public final class Artifacts {

	private Artifacts() {
		throw new AssertionError("Private Constructor.");
	}
	
	/**
	 * Creates a new Artifact object.
	 * 
	 * @param parts The list of {@link Part} objects forming the artifact's content.
	 * @param name The human-readable name of the artifact.
	 * @param description An optional description of the artifact.
	 * @return A new {@link Artifact} object with a generated artifact ID.
	 */
	public static Artifact newArtifact(final List<Part> parts, final String name, final String description) {
		
		return new Artifact.Builder()
						.artifactId(UUID.randomUUID().toString())
						.parts(Objects.requireNonNull(parts, "A list of parts is required."))
						.name(Objects.requireNonNull(name, "A human-readable name of the artifact is required."))
						.description(description)
						.build();
	}
	
	/**
	 * Creates a new {@link Artifact} object containing only a single {@link TextPart}.
	 * 
	 * @param name The human-readable name of the artifact.
	 * @param text The text content of the artifact.
	 * @param description An optional description of the artifact.
	 * @return A new {@link Artifact} object with a generated artifact ID.
	 */
	public static Artifact newTextArtifact(final String name, final String text, final String description) {
		
		return new Artifact.Builder()
						.parts(Arrays.asList(new TextPart(text)))
						.name(name)
						.description(description)
						.build();
	}
	
	/**
	 * Creates a new {@link Artifact} object containing only a single {@link DataPart}.
	 * 
	 * @param name The human-readable name of the artifact.
	 * @param data The structured data content of the artifact.
	 * @param description An optional description of the artifact.
	 * @return A new {@link Artifact} object with a generated artifact ID.
	 */
	public static Artifact newDataArtifact(final String name, final Map<String, Object> data, final String description) {
		
		return new Artifact.Builder()
						.parts(Arrays.asList(new DataPart(data)))
						.name(name)
						.description(description)
						.build();
	}
	
	/**
	 * Extracts and joins all text content from a Artifact's parts.
	 * 
	 * @param artifact The {@link Artifact} object.
	 * @return A single string containing all text content, or an empty string if no text parts are found.
	 */
	public static String getArtifactText(final Artifact artifact) {
		return getArtifactText(artifact, null);
	}
	
	/**
	 * Extracts and joins all text content from a Artifact's parts.
	 * 
	 * @param artifact The {@link Artifact} object.
	 * @param delimiter The string to use when joining text from multiple TextParts.
	 * @return  A single string containing all text content, or an empty string if no text parts are found.
	 */
	public static String getArtifactText(final Artifact artifact, final String delimiter) {
		String delim = delimiter;
		if (delim == null) delim = "\n";
		
		return String.join(delim, Parts.getTextParts(Arrays.asList(artifact.parts())));
	}
}
