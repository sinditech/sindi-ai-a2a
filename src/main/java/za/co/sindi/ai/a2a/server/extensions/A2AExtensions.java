/**
 * 
 */
package za.co.sindi.ai.a2a.server.extensions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.AgentExtension;

/**
 * @author Buhake Sindi
 * @since 06 November 2025
 */
public final class A2AExtensions {
	
	public static final String HTTP_EXTENSION_HEADER = "X-A2A-Extensions";

	private A2AExtensions() {
		throw new AssertionError("Private Constructor.");
	}
	
	public static Set<String> getRequestedExtensions(final List<String> values) {
		if (values == null || values.isEmpty()) return Collections.emptySet();
		
		return values.stream().flatMap(v -> Arrays.stream(v.split(","))).map(part -> part.trim()).collect(Collectors.toSet());
	}
	
	public static AgentExtension findExtensionByUri(final AgentCard card, final String uri) {
		if (card.getCapabilities() == null || card.getCapabilities().extensions() == null) return null;
		
		return Arrays.stream(card.getCapabilities().extensions()).filter(extension -> extension.getUri().equals(uri)).findFirst().orElse(null);
	}
}
