/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
@JsonbTypeInfo(
	key = "kind",
	value = {
	    @JsonbSubtype(alias="text", type=TextPart.class),
	    @JsonbSubtype(alias="file", type=FilePart.class),
	    @JsonbSubtype(alias="data", type=DataPart.class),
	}
)
public sealed interface Part extends Serializable permits PartBase {

}
