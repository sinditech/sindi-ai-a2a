/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Objects;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public record ListTasksResult(@JsonbProperty("tasks") Task[] tasks, @JsonbProperty("totalSize") long totalSize, @JsonbProperty("pageSize") long pageSize, @JsonbProperty("nextPageToken") String nextPageToken) {
	
	public ListTasksResult {
		tasks = Objects.requireNonNull(tasks,"Tasks are required.");
		if (totalSize < 0) throw new IllegalArgumentException("Illegal totalSize value.");
		if (pageSize < 0) throw new IllegalArgumentException("Illegal pageSize value.");
		if (pageSize > totalSize) throw new IllegalArgumentException("pageSize is greater than totalSize");
		nextPageToken = Objects.requireNonNull(nextPageToken, "Token for retrieving the next page is required.");
	}
}
