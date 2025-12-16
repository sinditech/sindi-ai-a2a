/**
 * 
 */
package za.co.sindi.ai.a2a.client;

import java.net.http.HttpClient;
import java.util.List;

import za.co.sindi.ai.a2a.types.PushNotificationConfig;
import za.co.sindi.ai.a2a.types.TransportProtocol;

/**
 * Configuration class for the A2AClient Factory.
 * 
 * @author Buhake Sindi
 * @since 04 November 2025
 */
public record ClientConfig(boolean streaming, boolean polling, HttpClient httpClient,
			List<TransportProtocol> supportedTransports, boolean useClientPreference, List<String> acceptedOutputModes,
			List<PushNotificationConfig> pushNotificationConfigs, List<String> extensions) {
		
	
	public static final class ClientConfigBuilder {
		private boolean streaming = true;
		private boolean polling = false;
		private HttpClient httpClient;
		private List<TransportProtocol> supportedTransports;
		private boolean useClientPreference = false;
		private List<String> acceptedOutputModes;
		private List<PushNotificationConfig> pushNotificationConfigs;
		private List<String> extensions;
		
		/**
		 * @param streaming the streaming to set
		 */
		public ClientConfigBuilder streaming(boolean streaming) {
			this.streaming = streaming;
			return this;
		}

		/**
		 * @param polling the polling to set
		 */
		public ClientConfigBuilder polling(boolean polling) {
			this.polling = polling;
			return this;
		}

		/**
		 * @param httpClient the httpClient to set
		 */
		public ClientConfigBuilder httpClient(HttpClient httpClient) {
			this.httpClient = httpClient;
			return this;
		}

		/**
		 * @param supportedTransports the supportedTransports to set
		 */
		public ClientConfigBuilder supportedTransports(List<TransportProtocol> supportedTransports) {
			this.supportedTransports = supportedTransports;
			return this;
		}
		
		/**
		 * @param supportedTransports the supportedTransports to set
		 */
		public ClientConfigBuilder supportedTransports(TransportProtocol... supportedTransports) {
			return supportedTransports(List.of(supportedTransports));
		}

		/**
		 * @param useClientPreference the useClientPreference to set
		 */
		public ClientConfigBuilder useClientPreference(boolean useClientPreference) {
			this.useClientPreference = useClientPreference;
			return this;
		}

		/**
		 * @param acceptedOutputModes the acceptedOutputModes to set
		 */
		public ClientConfigBuilder acceptedOutputModes(List<String> acceptedOutputModes) {
			this.acceptedOutputModes = acceptedOutputModes;
			return this;
		}
		
		/**
		 * @param acceptedOutputModes the acceptedOutputModes to set
		 */
		public ClientConfigBuilder acceptedOutputModes(String... acceptedOutputModes) {
			return acceptedOutputModes(List.of(acceptedOutputModes));
		}

		/**
		 * @param pushNotificationConfigs the pushNotificationConfigs to set
		 */
		public ClientConfigBuilder pushNotificationConfigs(List<PushNotificationConfig> pushNotificationConfigs) {
			this.pushNotificationConfigs = pushNotificationConfigs;
			return this;
		}
		
		/**
		 * @param pushNotificationConfigs the pushNotificationConfigs to set
		 */
		public ClientConfigBuilder pushNotificationConfigs(PushNotificationConfig... pushNotificationConfigs) {
			return pushNotificationConfigs(List.of(pushNotificationConfigs));
		}

		/**
		 * @param extensions the extensions to set
		 */
		public ClientConfigBuilder extensions(List<String> extensions) {
			this.extensions = extensions;
			return this;
		}
		
		/**
		 * @param extensions the extensions to set
		 */
		public ClientConfigBuilder extensions(String... extensions) {
			return extensions(List.of(extensions));
		}

		public ClientConfig build() {
			return new ClientConfig(streaming, polling, httpClient, supportedTransports, useClientPreference, acceptedOutputModes, pushNotificationConfigs, extensions);
		}
	}
}
