/**
 * 
 */
package za.co.sindi.ai.a2a.server;

/**
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class IDGeneratorContext {

	private final String taskId;
	private final String contextId;
	
	/**
	 * @param taskId
	 * @param contextId
	 */
	public IDGeneratorContext(String taskId, String contextId) {
		super();
		this.taskId = taskId;
		this.contextId = contextId;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}
}
