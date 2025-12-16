/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import za.co.sindi.ai.a2a.utils.json.JsonAPISchemeInAdapter;
import za.co.sindi.ai.a2a.utils.json.JsonJSONRPCVersionAdapter;
import za.co.sindi.ai.a2a.utils.json.JsonMessageRoleAdapter;
import za.co.sindi.ai.a2a.utils.json.JsonRequestIdSerialization;
import za.co.sindi.ai.a2a.utils.json.JsonTaskStateAdapter;
import za.co.sindi.ai.a2a.utils.json.JsonTransportProtocolAdapter;

/**
 * @author Buhake Sindi
 * @since 03 November 2025
 */
public final class JsonUtils {

	private JsonUtils() {
		throw new AssertionError("Private constructor.");
	}
	
	private static void registerConfig(final JsonbConfig config) {
		config.withAdapters(new JsonAPISchemeInAdapter(),
				new JsonJSONRPCVersionAdapter(),
				new JsonMessageRoleAdapter(),
				new JsonTaskStateAdapter(),
				new JsonTransportProtocolAdapter())
			.withSerializers(new JsonRequestIdSerialization())
			.withDeserializers(new JsonRequestIdSerialization());
	}
	
	public static <T> String marshall(final T object) {
		return marshall(object, false);
	}
	
	public static <T> String marshall(final T object, boolean prettyPrint) {
		JsonbConfig config = new JsonbConfig().withFormatting(prettyPrint);
		registerConfig(config);
		return JsonbBuilder.create(config).toJson(object);
	}
	
	public static <T> T unmarshall(final String value, final Class<T> clazz) {
		JsonbConfig config = new JsonbConfig();
		registerConfig(config);
		return JsonbBuilder.create(config).fromJson(value, clazz);
	}
}
