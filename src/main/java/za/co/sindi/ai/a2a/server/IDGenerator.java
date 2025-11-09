/**
 * 
 */
package za.co.sindi.ai.a2a.server;

import java.util.UUID;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
@FunctionalInterface
public interface IDGenerator {
	
	public static final IDGenerator DEFAULT = (context) -> UUID.randomUUID().toString(); 

	public String generateId(final IDGeneratorContext context);
}
