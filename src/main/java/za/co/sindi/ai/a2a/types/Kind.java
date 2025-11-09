/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

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
	}
)
public sealed interface Kind extends Serializable permits Message, Task {

}
