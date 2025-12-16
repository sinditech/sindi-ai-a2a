/**
 * 
 */
package za.co.sindi.ai.a2a.server.agentexecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.IDGenerator;
import za.co.sindi.ai.a2a.server.IDGeneratorContext;
import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.types.InvalidParamsError;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.MessageSendConfiguration;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.utils.Messages;

/**
 * Request Context.<p />
 * 
 * Holds information about the current request being processed by the server,
 * including the incoming message, task and context identifiers, and related
 * tasks.
    
 * @author Buhake Sindi
 * @since 23 October 2025
 */
public class RequestContext {

	private MessageSendParams params;
	private Task task;
	private List<Task> relatedTasks;
	private String taskId;
	private String contextId;
	private ServerCallContext callContext;
	private IDGenerator taskIdGenerator;
	private IDGenerator contextIdGenerator;
	
	/**
	 * @param params
	 * @param task
	 * @param relatedTasks
	 * @param taskId
	 * @param contextId
	 * @param callContext
	 */
	public RequestContext(MessageSendParams params, Task task, List<Task> relatedTasks, String taskId, String contextId,
			ServerCallContext callContext) {
		this(params, task, relatedTasks, taskId, contextId, callContext, null, null);
	}
	
	/**
	 * @param params
	 * @param task
	 * @param relatedTasks
	 * @param taskId
	 * @param contextId
	 * @param callContext
	 * @param taskIdGenerator
	 * @param contextIdGenerator
	 */
	public RequestContext(MessageSendParams params, Task task, List<Task> relatedTasks, String taskId, String contextId,
			ServerCallContext callContext, IDGenerator taskIdGenerator, IDGenerator contextIdGenerator) {
		super();
		this.params = params;
		this.task = task;
		this.relatedTasks = relatedTasks;
		if (this.relatedTasks == null) this.relatedTasks = new ArrayList<>();
		this.taskId = taskId;
		this.contextId = contextId;
		this.callContext = callContext;
		this.taskIdGenerator = taskIdGenerator;
		if (this.taskIdGenerator == null) this.taskIdGenerator = IDGenerator.DEFAULT;
		this.contextIdGenerator = contextIdGenerator;
		if (this.contextIdGenerator == null) this.contextIdGenerator = IDGenerator.DEFAULT;
		
		if (this.params != null) {
			if (taskId != null && task != null && !task.getId().equals(taskId)) {
				throw new A2AServerError(new InvalidParamsError("bad task id."));
			} else checkOrGenerateTaskId();
			
			if (contextId != null && task != null && !task.getContextId().equals(contextId)) {
				throw new A2AServerError(new InvalidParamsError("bad context id."));
			} else checkOrGenerateContextId();
		}
	}
	
	private void checkOrGenerateTaskId() {
		if (params == null) return;
		
		if (taskId == null && params.message().getTaskId() == null) {
			params.message().setTaskId(taskIdGenerator.generateId(new IDGeneratorContext(null, contextId)));
		}
		
		if (params.message().getTaskId() != null) {
			taskId = params.message().getTaskId();
		}
	}
	
	private void checkOrGenerateContextId() {
		if (params == null) return;
		
		if (contextId == null && params.message().getContextId() == null) {
			params.message().setContextId(taskIdGenerator.generateId(new IDGeneratorContext(taskId, null)));
		}
		
		if (params.message().getContextId() != null) {
			contextId = params.message().getContextId();
		}
	}
	
	public String getUserInput(final String delimiter) {
		if (params == null) return "";
		
		return Messages.getMessageText(params.message(), delimiter);
	}
	
	public void attachRelatedTask(final Task task) {
		if (task != null) this.relatedTasks.add(task);
	}
	
	public Message getMessage() {
		return (params != null) ? params.message() : null;
	}
	
	public List<Task> getRelatedTasks() {
		return relatedTasks;
	}
	
	public Task getTask() {
		return task;
	}
	
//	public void setTask(final Task task) {
//		this.task = task;
//	}
	
	public String getTaskId() {
		return taskId;
	}
	
	public String getContextId() {
		return contextId;
	}
	
	public MessageSendConfiguration getConfiguration() {
		return (params == null) ? null : params.configuration();
	}
	
	public ServerCallContext getCallContext() {
		return callContext;
	}
	
	public Map<String, Object> getMetadata() {
		return (params == null) ? Map.of() : params.metadata();
	}
	
	public void addActivatedExtension(final String url) {
		if (callContext != null) {
			callContext.getActivatedExtensions().add(url);
		}
	}
	
	public Set<String> getRequestedExtensions() {
		if (callContext != null) {
			return callContext.getRequestedExtensions();
		}
		
		return Set.of();
	}
	
	public static class RequestContextBuilder {
		private MessageSendParams params;
		private Task task;
		private List<Task> relatedTasks;
		private String taskId;
		private String contextId;
		private ServerCallContext callContext;
		private IDGenerator taskIdGenerator;
		private IDGenerator contextIdGenerator;
		
		/**
		 * @param params the params to set
		 */
		public RequestContextBuilder params(MessageSendParams params) {
			this.params = params;
			return this;
		}
		
		/**
		 * @return the params
		 */
		protected MessageSendParams getParams() {
			return params;
		}

		/**
		 * @param task the task to set
		 */
		public RequestContextBuilder task(Task task) {
			this.task = task;
			return this;
		}
		
		/**
		 * @param relatedTasks the relatedTasks to set
		 */
		public RequestContextBuilder relatedTasks(List<Task> relatedTasks) {
			this.relatedTasks = relatedTasks;
			return this;
		}
		/**
		 * @param taskId the taskId to set
		 */
		public RequestContextBuilder taskId(String taskId) {
			this.taskId = taskId;
			return this;
		}
		
		/**
		 * @param contextId the contextId to set
		 */
		public RequestContextBuilder contextId(String contextId) {
			this.contextId = contextId;
			return this;
		}
		/**
		 * @param callContext the callContext to set
		 */
		public RequestContextBuilder callContext(ServerCallContext callContext) {
			this.callContext = callContext;
			return this;
		}
		
		/**
		 * @param taskIdGenerator the taskIdGenerator to set
		 */
		public RequestContextBuilder taskIdGenerator(IDGenerator taskIdGenerator) {
			this.taskIdGenerator = taskIdGenerator;
			return this;
		}
		
		/**
		 * @param contextIdGenerator the contextIdGenerator to set
		 */
		public RequestContextBuilder contextIdGenerator(IDGenerator contextIdGenerator) {
			this.contextIdGenerator = contextIdGenerator;
			return this;
		}
		
		public RequestContext build() {
			return new RequestContext(params, task, relatedTasks, taskId, contextId, callContext, taskIdGenerator, contextIdGenerator);
		}
	}
}
