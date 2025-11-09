/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 15 October 2025
 */
@JsonbTypeInfo(
	key = "kind",
	value = {
	    @JsonbSubtype(alias=TaskStatusUpdateEvent.KIND, type=TaskStatusUpdateEvent.class),
	    @JsonbSubtype(alias=TaskArtifactUpdateEvent.KIND, type=TaskArtifactUpdateEvent.class),
	}
)
public sealed interface UpdateEvent extends Event permits TaskStatusUpdateEvent, TaskArtifactUpdateEvent {

}
