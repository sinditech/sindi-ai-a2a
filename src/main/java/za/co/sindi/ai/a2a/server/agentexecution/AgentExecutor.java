/**
 * 
 */
package za.co.sindi.ai.a2a.server.agentexecution;

import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskArtifactUpdateEvent;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatusUpdateEvent;

/**
 * Agent Executor interface.<p />
 * 
 * Implementations of this interface contain the core logic of the agent,
 * executing tasks based on requests and publishing updates to an event queue.
 * 
 * @author Buhake Sindi
 * @since 28 October 2025
 */
public interface AgentExecutor {

	/**
	 * Execute the agent's logic for a given request context. <p />
	 * 
	 * The agent should read necessary information from the <code>context</context> and
     * publish {@link Task} or {@link Message} events, or {@link TaskStatusUpdateEvent} /
     * {@link TaskArtifactUpdateEvent} to the <code>eventQueue</code>. <br />
     * This method should return once the agent's execution for this request is complete or
     * yields control (e.g., enters an input-required state).
     *    
	 * @param context The request context containing the message, task ID, etc.
	 * @param eventQueue The queue to publish events to.
	 */
	public void execute(final RequestContext context, final EventQueue eventQueue);
	
	/**
	 * Request the agent to cancel an ongoing task. <p />
	 * 
	 * The agent should attempt to stop the task identified by the <code>taskId</code>
     * in the <code>context</code> and publish a {@link TaskStatusUpdateEvent} with state
     * {@link TaskState}.CANCELED to the <code>eventQueue</code>.
     *    
	 * @param context The request context containing the task ID to cancel.
	 * @param eventQueue The queue to publish the cancellation status update to.
	 */
	public void cancel(final RequestContext context, final EventQueue eventQueue);
}
