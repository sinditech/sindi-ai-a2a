/**
 * 
 */
package za.co.sindi.ai.a2a.extensions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
	
	/**
	 * Get the set of requested extensions from an input list.
	 * 
	 * This handles the list containing potentially comma-separated values, as occurs when using a list in an HTTP header.
	 * 
	 * @param values
	 * @return
	 */
	public static Set<String> getRequestedExtensions(final List<String> values) {
		if (values == null || values.isEmpty()) return Collections.emptySet();
		
		return values.stream().flatMap(v -> Arrays.stream(v.split(","))).map(part -> part.trim()).collect(Collectors.toSet());
	}
	
	public static AgentExtension findExtensionByUri(final AgentCard card, final String uri) {
		if (card.getCapabilities() == null || card.getCapabilities().extensions() == null) return null;
		
		return Arrays.stream(card.getCapabilities().extensions()).filter(extension -> extension.getUri().equals(uri)).findFirst().orElse(null);
	}
	
	/**
	 * Update the X-A2A-Extensions header with active extensions.
	 * 
	 * @param httpKeywordArguments
	 * @param extensions
	 */
	public static Map<String, Object> updateExtensionHeader(final Map<String, Object> httpKeywordArguments, final List<String> extensions) {
		Map<String, Object> httpKeywordArgs = httpKeywordArguments == null ? new ConcurrentHashMap<>() : httpKeywordArguments;
		if (extensions != null && !extensions.isEmpty()) {
			@SuppressWarnings("unchecked")
			Map<String, String> headers = (Map<String, String>)httpKeywordArgs.getOrDefault("headers", new ConcurrentHashMap<String, String>());
			headers.put(HTTP_EXTENSION_HEADER, String.join(",", extensions));
		}
		
		return httpKeywordArgs;
	}
}
