/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public sealed interface Event extends Serializable permits Message, Task, UpdateEvent {

}
