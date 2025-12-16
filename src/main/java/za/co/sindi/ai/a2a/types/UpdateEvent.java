/**
 * 
 */
package za.co.sindi.ai.a2a.types;

/**
 * @author Buhake Sindi
 * @since 15 October 2025
 */
public sealed interface UpdateEvent extends Event permits TaskStatusUpdateEvent, TaskArtifactUpdateEvent {

}
