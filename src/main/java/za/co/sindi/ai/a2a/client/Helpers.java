/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.util.UUID;

import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Message.Role;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.TextPart;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public final class Helpers {

	private Helpers() {
		throw new AssertionError("Private constructor.");
	}
	
	public static Message createTextMessageObject(final String content) {
		return createTextMessageObject(Role.USER, content);
	}
	
	public static Message createTextMessageObject(final Role role, final String content) {
		return new Message(role == null ? Role.USER : role, new Part[] { new TextPart(content) }, UUID.randomUUID().toString());
	}
}
