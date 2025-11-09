/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.UpdateEvent;

/**
 * Alias for emitted events from client
 * 
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public record ClientEvent(Task task, UpdateEvent updateEvent) {

}
