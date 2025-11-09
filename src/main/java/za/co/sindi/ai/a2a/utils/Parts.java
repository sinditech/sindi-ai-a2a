/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.List;

import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.TextPart;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public final class Parts {

	private Parts() {
		throw new AssertionError("Private Constructor.");
	}
	
	public static List<String> getTextParts(final List<Part> parts) {
		return parts.stream().filter(part -> part instanceof TextPart).map(part -> ((TextPart)part).getText()).toList();
	}
}
