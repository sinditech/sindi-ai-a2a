/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import za.co.sindi.ai.a2a.types.PushNotificationConfig;
import za.co.sindi.ai.a2a.types.PushNotificationConfig.Builder;

/**
 * @author Buhake Sindi
 * @since 27 October 2025
 */
public class InMemoryPushNotificationConfigStore implements PushNotificationConfigStore {

	private Map<String, List<PushNotificationConfig>> pushNotificationInfos = new ConcurrentHashMap<>();
	
	/**
	 * Sets or updates the push notification configuration for a task in memory.
	 */
	@Override
	public void setInfo(String taskId, PushNotificationConfig notificationConfig) {
		// TODO Auto-generated method stub
		List<PushNotificationConfig> configs = pushNotificationInfos.getOrDefault(taskId, new ArrayList<>());
		Builder builder = new Builder(notificationConfig);
		if (notificationConfig.id() == null || notificationConfig.id().isEmpty()) {
			builder.id(taskId);
		}
		
		PushNotificationConfig _notificationConfig = builder.build();
		Iterator<PushNotificationConfig> configIterator = configs.iterator();
		while(configIterator.hasNext()) {
			PushNotificationConfig config = configIterator.next();
			if (config.id().equals(_notificationConfig.id())) {
				configIterator.remove();
				break ;
			}
		}
		
		configs.add(_notificationConfig);
		pushNotificationInfos.put(taskId, configs);
	}

	/**
	 * Retrieves the push notification configuration for a task from memory.
	 */
	@Override
	public List<PushNotificationConfig> getInfo(String taskId) {
		// TODO Auto-generated method stub
		return pushNotificationInfos.getOrDefault(taskId, List.of());
	}

	/**
	 * Deletes the push notification configuration for a task from memory.
	 */
	@Override
	public void deleteInfo(String taskId, String configId) {
		// TODO Auto-generated method stub
		String _configId = configId;
		if (_configId == null) _configId = taskId;
		
		List<PushNotificationConfig> notificationConfigs = pushNotificationInfos.get(taskId);
		if (notificationConfigs == null || notificationConfigs.isEmpty()) return;
		
		Iterator<PushNotificationConfig> notificationConfigIterator = notificationConfigs.iterator();
		while (notificationConfigIterator.hasNext()) {
			PushNotificationConfig config = notificationConfigIterator.next();
			if (config.id().equals(_configId)) {
				notificationConfigIterator.remove();
				break ;
			}
		}

		if (notificationConfigs.isEmpty()) pushNotificationInfos.remove(taskId);
	}
}
