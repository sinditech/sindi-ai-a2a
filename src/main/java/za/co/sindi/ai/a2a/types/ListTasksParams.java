/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public record ListTasksParams(@JsonbProperty String contextId, @JsonbProperty TaskState status, @JsonbProperty Integer pageSize, @JsonbProperty String pageToken,
							  @JsonbProperty Integer historyLength, @JsonbProperty Long lastUpdatedAfter, @JsonbProperty Boolean includeArtifacts, @JsonbProperty Map<String, Object> metadata) {

}
