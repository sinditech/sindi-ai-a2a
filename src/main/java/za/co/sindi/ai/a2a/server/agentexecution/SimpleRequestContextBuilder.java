/**
 * 
 */
package za.co.sindi.ai.a2a.server.agentexecution;

import java.util.ArrayList;
import java.util.List;

import za.co.sindi.ai.a2a.server.agentexecution.RequestContext.RequestContextBuilder;
import za.co.sindi.ai.a2a.server.tasks.TaskStore;
import za.co.sindi.ai.a2a.types.Task;

/**
 * Builds request context and populates referred tasks.
 * 
 * @author Buhake Sindi
 * @since 28 October 2025
 */
public class SimpleRequestContextBuilder extends RequestContextBuilder {

	private TaskStore taskStore;
	private boolean shouldPopulateReferredTasks;

	/**
	 * @param taskStore
	 */
	public SimpleRequestContextBuilder(TaskStore taskStore) {
		this(taskStore, false);
	}

	/**
	 * @param taskStore
	 * @param shouldPopulateReferredTasks
	 */
	public SimpleRequestContextBuilder(TaskStore taskStore, boolean shouldPopulateReferredTasks) {
		super();
		this.taskStore = taskStore;
		this.shouldPopulateReferredTasks = shouldPopulateReferredTasks;
	}

	@Override
	public RequestContext build() {
		// TODO Auto-generated method stub
		List<Task> relatedTasks = null;
		if (taskStore != null && shouldPopulateReferredTasks && getParams() != null && getParams().message().getReferenceTaskIds() != null) {
			relatedTasks = new ArrayList<>();
            for (String taskId : getParams().message().getReferenceTaskIds()) {
                Task task = taskStore.load(taskId);
                if (task != null) {
                    relatedTasks.add(task);
                }
            }
		}
		
		relatedTasks(relatedTasks);
		return super.build();
	}
}
