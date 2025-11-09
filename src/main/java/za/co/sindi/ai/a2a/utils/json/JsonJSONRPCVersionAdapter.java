/**
 * 
 */
package za.co.sindi.ai.a2a.utils.json;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.a2a.types.JSONRPCVersion;

/**
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class JsonJSONRPCVersionAdapter implements JsonbAdapter<JSONRPCVersion, String> {

	@Override
	public String adaptToJson(JSONRPCVersion version) throws Exception {
		// TODO Auto-generated method stub
		if (version == null) return null;
		return version.toString();
	}

	@Override
	public JSONRPCVersion adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return JSONRPCVersion.of(value);
	}
}
