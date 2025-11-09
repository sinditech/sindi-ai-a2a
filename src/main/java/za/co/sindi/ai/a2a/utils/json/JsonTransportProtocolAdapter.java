/**
 * 
 */
package za.co.sindi.ai.a2a.utils.json;

import jakarta.json.bind.adapter.JsonbAdapter;
import za.co.sindi.ai.a2a.types.TransportProtocol;

/**
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class JsonTransportProtocolAdapter implements JsonbAdapter<TransportProtocol, String> {

	@Override
	public String adaptToJson(TransportProtocol protocol) throws Exception {
		// TODO Auto-generated method stub
		if (protocol == null) return null;
		return protocol.toString();
	}

	@Override
	public TransportProtocol adaptFromJson(String value) throws Exception {
		// TODO Auto-generated method stub
		if (value == null) return null;
		return TransportProtocol.of(value);
	}
}
