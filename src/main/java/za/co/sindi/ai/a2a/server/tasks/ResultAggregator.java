/**
 * 
 */
package za.co.sindi.ai.a2a.server.tasks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.server.agentexecution.AgentExecutor;
import za.co.sindi.ai.a2a.server.events.EventConsumer;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.Kind;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatusUpdateEvent;
import za.co.sindi.commons.concurrent.ConsumingSubscriber;
import za.co.sindi.commons.concurrent.TransformingPublisher;

/**
 * ResultAggregator is used to process the event streams from an {@link AgentExecutor}.<p />
 *
 * There are three main ways to use the ResultAggregator:
 * <ol>
 * 	<li> As part of a processing pipe, <code>consumeAndEmit</code> will construct the updated
 *    task as the events arrive, and re-emit those events for another consumer</li>
 * 	<li> As part of a blocking call, <code>consumeAll</code> will process the entire stream and
 *    return the final {@link Task} or {@link Message} object</li>
 * 	<li> As part of a push solution where the latest Task is emitted after processing an event,
 *    <code>consumeAndEmitTask</code> will consume the {@link Event} stream, process the events to the current
 *    {@link Task} object and emit that {@link Task} object.</li>
 * </ol>
 *    
 * @author Buhake Sindi
 * @since 28 October 2025
 */
public class ResultAggregator {
	private static final Logger LOGGER = Logger.getLogger(ResultAggregator.class.getName());
	
	private final TaskManager taskManager;
	private Message message;
	
	/**
	 * Initializes the ResultAggregator.
	 * 
	 * @param taskManager The {@link TaskManager} instance to use for processing events
                          and managing the task state.
	 */
	public ResultAggregator(TaskManager taskManager) {
		this(taskManager, null);
	}
	
	/**
	 * Initializes the ResultAggregator.
	 * 
	 * @param taskManager The {@link TaskManager} instance to use for processing events
                          and managing the task state.
	 * @param message
	 */
	public ResultAggregator(TaskManager taskManager, Message message) {
		super();
		this.taskManager = Objects.requireNonNull(taskManager, "TaskManager is required.");
		this.message = message;
	}

	/**
	 * Returns the current aggregated result ({@link Task} or {@link Message}).
	 * 
	 * @return The current {@link Task} object managed by the {@link TaskManager}, or the final
           {@link Message} if one was received, or <code>null</code>, if no result has been produced yet.
	 */
	public Kind getCurrentResult() {
		if (message != null) return message;
		
		return taskManager.getTask();
	}
	
	/**
	 * Processes the event stream from the consumer, updates the task state, and re-emits the same events.
	 * 
	 * Useful for streaming scenarios where the server needs to observe and
     * process events (e.g., save task state, send push notifications) while
     * forwarding them to the client.
     *    
	 * @param consumer The {@link EventConsumer} to read events from.
	 * @return  The {@link Event} objects consumed from the {@link EventConsumer}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Publisher<Event> consumeAndEmit(final EventConsumer consumer) {
		Publisher<Event> all = consumer.consumeAll();
		return new TransformingPublisher(all, (event) -> {
			return taskManager.process((Event) event);
		});
	}
	
	/**
	 * Processes the entire event stream from the consumer and returns the final result.
	 * @param consumer The {@link EventConsumer} to read events from.
	 * @return The final {@link Task} object or {@link Message} object after the stream is exhausted.
            Returns <code>null</code> if the stream ends without producing a final result.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Event consumeAll(final EventConsumer consumer) {
		final AtomicReference<Event> returnEvent = new AtomicReference<>();
		Publisher<Event> all = consumer.consumeAll();
		all.subscribe(new ConsumingSubscriber(event -> {
			if (event instanceof Message message) {
				this.message = message;
				if (returnEvent.get() == null) {
					returnEvent.set((Event)event);
				}
			} else {
				taskManager.process((Event) event);
			}
		}));
		
		if (returnEvent.get() != null) {
			return returnEvent.get();
		}
		
		return taskManager.getTask();
	}
	
	/**
	 * Processes the event stream until completion or an interruptable state is encountered.
	 * 
	 * @param consumer The {@link EventConsumer} to read events from.
	 * @param blocking If <code>false</code>, the method returns as soon as a task/message
     *                  is available. If <code>true</code>, it waits for a terminal state.
	 * @param eventCallback Optional async callback function to be called after each event
     *                       is processed in the background continuation.
     *                       Mainly used for push notifications currently.
	 * @return A tuple containing:
     *        <ul>
     *          <li>The current aggregated result ({@link Task} or {@link Message}) at the point of completion or interruption.</li>
     *          <li>A boolean indicating whether the consumption was interrupted, <code>true</code> or completed naturally, <code>false</code>.</li>
     *        </ul>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EventInterrupt consumeAndBreakOnInterrupt(final EventConsumer consumer, boolean blocking, Runnable eventCallback) {
		Publisher<Event> all = consumer.consumeAll();
		final AtomicBoolean interrupted = new AtomicBoolean(false);
		final AtomicReference<EventInterrupt> returnEventInterrupt = new AtomicReference<>();
		
		all.subscribe(new ConsumingSubscriber(event -> {
			if (event instanceof Message message) {
				this.message = message;
				if (returnEventInterrupt.get() == null) {
					returnEventInterrupt.set(new EventInterrupt(message, false));
				}
			} else if (!interrupted.get()){
				taskManager.process((Event) event);
			
				boolean shouldInterrupt = false;
				boolean isAuthRequired = (event instanceof Task task && task.getStatus().state() == TaskState.AUTH_REQUIRED)
                        || (event instanceof TaskStatusUpdateEvent tsue && tsue.getStatus().state() == TaskState.AUTH_REQUIRED);
				
				if (isAuthRequired) {
					LOGGER.fine("Encountered an auth-required task: breaking synchronous message/send flow.");
					shouldInterrupt = true;
				} else if (!blocking) {
					LOGGER.fine("Non-blocking call: returning task after first event.");
					shouldInterrupt = true;
				}
				
				if (shouldInterrupt) {
					// Continue consuming the rest of the events in the background.
					CompletableFuture.runAsync(() -> continueConsuming(all, eventCallback));
					interrupted.set(true);
				}
			}
		}));
		
		if (returnEventInterrupt.get() != null) {
			return returnEventInterrupt.get();
		}
		
		return new EventInterrupt(taskManager.getTask(), interrupted.get());
	}
	
	/**
	 * Continues processing an event stream in a background task.
	 * 
	 * @param eventPublisher The remaining producer of events from the consumer.
	 * @param eventCallback Optional async callback function to be called after each event is processed.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void continueConsuming(final Publisher<Event> eventPublisher, final Runnable eventCallback) {
		eventPublisher.subscribe(new ConsumingSubscriber(event -> {
			taskManager.process((Event)event);
			if (eventCallback != null) eventCallback.run();
		}));
	}
	
	public static final record EventInterrupt(Kind result, boolean interrupted) {}
}
