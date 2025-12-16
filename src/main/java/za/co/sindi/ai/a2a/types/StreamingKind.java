/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 23 October 2025
 */
@JsonbTypeInfo(
	key = "kind",
	value = {
		@JsonbSubtype(alias=Message.KIND, type=Message.class),
		@JsonbSubtype(alias=Task.KIND, type=Task.class),
	    @JsonbSubtype(alias=TaskStatusUpdateEvent.KIND, type=TaskStatusUpdateEvent.class),
	    @JsonbSubtype(alias=TaskArtifactUpdateEvent.KIND, type=TaskArtifactUpdateEvent.class),
	}
)
public sealed interface StreamingKind extends Kind permits Message, Task, TaskStatusUpdateEvent, TaskArtifactUpdateEvent {

}
