/**
 * 
 */
package za.co.sindi.ai.a2a.client.middleware;

import java.util.Map;

/**
 * A context passed with each client call, allowing for call-specific.
 *
 * configuration and data passing. Such as authentication details or
 * request deadlines.
 * 
 * @author Buhake Sindi
 * @since 31 October 2025
 */
public record ClientCallContext(Map<String, Object> state) {

}
