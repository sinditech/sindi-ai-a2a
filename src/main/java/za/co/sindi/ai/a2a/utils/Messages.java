/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.Arrays;

import za.co.sindi.ai.a2a.types.Message;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public final class Messages {

	private Messages() {
		throw new AssertionError("Private Constructor.");
	}
	
	public static String getMessageText(final Message message, final String delimiter) {
		String delim = delimiter;
		if (delim == null) delim = "\n";
		
		return String.join(delim, Parts.getTextParts(Arrays.asList(message.getParts())));
	}
}
