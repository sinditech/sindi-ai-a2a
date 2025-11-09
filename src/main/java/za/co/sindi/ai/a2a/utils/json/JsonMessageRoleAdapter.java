/**
 * 
 */
package za.co.sindi.ai.a2a.utils.json;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.a2a.types.Message.Role;

/**
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class JsonMessageRoleAdapter implements JsonbAdapter<Role, String> {

	@Override
	public String adaptToJson(Role format) throws Exception {
		// TODO Auto-generated method stub
		if (format == null) return null;
		return format.toString();
	}

	@Override
	public Role adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return Role.of(value);
	}
}
