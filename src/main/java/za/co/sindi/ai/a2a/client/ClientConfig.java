/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.util.List;

import za.co.sindi.ai.a2a.types.PushNotificationConfig;

/**
 * Configuration class for the A2AClient Factory.
 * 
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public class ClientConfig {

	private boolean streaming = true;
	private boolean polling = false;
	private boolean useClientPreference = false;
	private List<String> acceptedOutputModes;
	private List<PushNotificationConfig> pushNotificationConfigs;
	
	/**
	 * @param streaming
	 * @param polling
	 * @param useClientPreference
	 * @param acceptedOutputModes
	 * @param pushNotificationConfigs
	 */
	public ClientConfig(boolean streaming, boolean polling, boolean useClientPreference,
			List<String> acceptedOutputModes, List<PushNotificationConfig> pushNotificationConfigs) {
		super();
		this.streaming = streaming;
		this.polling = polling;
		this.useClientPreference = useClientPreference;
		this.acceptedOutputModes = acceptedOutputModes;
		this.pushNotificationConfigs = pushNotificationConfigs;
	}

	/**
	 * @return the streaming
	 */
	public boolean isStreaming() {
		return streaming;
	}

	/**
	 * @return the polling
	 */
	public boolean isPolling() {
		return polling;
	}

	/**
	 * @return the useClientPreference
	 */
	public boolean isUseClientPreference() {
		return useClientPreference;
	}

	/**
	 * @return the acceptedOutputModes
	 */
	public List<String> getAcceptedOutputModes() {
		return acceptedOutputModes;
	}

	/**
	 * @return the pushNotificationConfigs
	 */
	public List<PushNotificationConfig> getPushNotificationConfigs() {
		return pushNotificationConfigs;
	}
}
