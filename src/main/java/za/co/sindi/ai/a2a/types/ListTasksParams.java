/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public record ListTasksParams(String contextId, TaskState status, Integer pageSize, String pageToken,
							  Integer historyLength, Long lastUpdatedAfter, Boolean includeArtifacts, Map<String, Object> metadata) {

	@JsonbCreator
	public static ListTasksParams create(@JsonbProperty("contextId") String contextId, @JsonbProperty("status") TaskState status, @JsonbProperty("pageSize") Integer pageSize, @JsonbProperty("pageToken") String pageToken,
			  @JsonbProperty("historyLength") Integer historyLength, @JsonbProperty("lastUpdatedAfter") Long lastUpdatedAfter, @JsonbProperty("includeArtifacts") Boolean includeArtifacts, @JsonbProperty("metadata") Map<String, Object> metadata) {
		return new ListTasksParams(contextId, status, pageSize, pageToken, historyLength, lastUpdatedAfter, includeArtifacts, metadata);
	}
}
