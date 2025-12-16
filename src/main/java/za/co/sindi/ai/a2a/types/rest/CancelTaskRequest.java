/**
 * 
 */
package za.co.sindi.ai.a2a.types.rest;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 18 November 2025
 */
public record CancelTaskRequest(String name) {

	@JsonbCreator
	public static CancelTaskRequest create(@JsonbProperty("name") String name) {
		return new CancelTaskRequest(name); 
	}
}
