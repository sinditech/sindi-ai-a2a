/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.events.EventQueue.QueueClosedException;
import za.co.sindi.ai.a2a.server.events.EventQueue.QueueEmptyException;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.Message;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskStatusUpdateEvent;

/**
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public class EventConsumer {
	private static final Logger LOGGER = Logger.getLogger(EventConsumer.class.getName());
	private static final int QUEUE_WAIT_MILLISECONDS = 500;
	
	private final EventQueue queue;
	private final long timeoutMillis;
	private Throwable exception;
	
	/**
	 * @param queue
	 */
	public EventConsumer(EventQueue queue) {
		this(queue, QUEUE_WAIT_MILLISECONDS);
	}
	
	/**
	 * @param queue
	 * @param timeoutMillis
	 */
	public EventConsumer(EventQueue queue, long timeoutMillis) {
		super();
		this.queue = Objects.requireNonNull(queue, "An EventQueue is required.");
		this.timeoutMillis = timeoutMillis;
	}

	/**
	 * Consume one event from the agent event queue non-blocking.
	 * 
	 * @return The next event from the queue.
	 */
	public Event consumeOne() {
		LOGGER.fine("Attempting to consume one event.");
		try {
			Event event = queue.dequeueEvent(true);
			LOGGER.info(String.format("Dequeued event of type: %s in consumeOne().", event.getClass().getSimpleName()));
			queue.taskDone();
			
			return event;
		} catch (InterruptedException | QueueClosedException | QueueEmptyException e) {
			// TODO Auto-generated catch block
			if (e instanceof QueueClosedException) {
				LOGGER.warning("Event queue is closed in consumeOne().");
			} else if (e instanceof QueueEmptyException) {
				LOGGER.warning("Event queue was empty in consumeOne().");
			} else {
				LOGGER.warning("Event queue threw and interrupted exception in consumeOne().");
			}
			throw new A2AServerError(new za.co.sindi.ai.a2a.types.InternalError("Agent did not return any response."));
		}
	}
	
	/**
	 * Consume all the generated streaming events from the agent.
	 * @return Events dequeued from the queue.
	 */
	public Publisher<Event> consumeAll() {
		LOGGER.fine("Starting to consume all events from the queue.");
        
		return subscriber -> {
			// Create a subscription that allows cancellation
            subscriber.onSubscribe(new Flow.Subscription() {
                private volatile boolean cancelled = false;
                
                @Override
                public void request(long n) {
                    // Backpressure is handled internally by the queue blocking
                    if (n <= 0 && !cancelled) {
                        subscriber.onError(new IllegalArgumentException(
                            "Request must be positive, was: " + n));
                    }
                }
                
                @Override
                public void cancel() {
                	if (!cancelled) {
                		LOGGER.fine("Subscription cancelled");
                		cancelled = true;
                	}
                }
            });
            
            consumeAll(subscriber);
		};
	}
	
	/**
	 * Callback to handle exceptions from the agent's execution task.
     * <p>
     * If the agent's CompletableFuture raises an exception, this callback is
     * invoked, and the exception is stored to be re-raised by the consumer loop.
     * 
	 * @param agentTask the task that completed.
	 */
	public void agentTaskCallback(FutureTask<?> agentTask) {
		LOGGER.fine("Agent task callback triggered.");
        if (!agentTask.isCancelled() && agentTask.isDone()) {
//            try {
//                agentTask.get(); // will throw if exception occurred
//            } catch (Exception e) {
//            	this.exception = Throwables.getRootCause(e);  //e.getCause() != null ? e.getCause() : e;
//            } 
        	this.exception = agentTask.exceptionNow();
        }
    }

	private boolean isFinalEvent(Event event) {
        if (event instanceof TaskStatusUpdateEvent tsue && tsue.isFinal()) return true;
        if (event instanceof Message) return true;
        if (event instanceof Task task) {
            return switch (task.getStatus().state()) {
                case COMPLETED, CANCELED, FAILED, REJECTED, UNKNOWN, INPUT_REQUIRED -> true;
                default -> false;
            };
        }
        return false;
    }
	
	private void consumeAll(Subscriber<? super Event> subscriber) {
		LOGGER.fine("Event pump started");
		
		while (true) {
            if (exception != null) {
            	LOGGER.log(Level.SEVERE, "Agent task exception detected", exception);
            	subscriber.onError(exception);
            	throw new CompletionException(exception);
            }

            try {
                Event event = queue.dequeueEvent(timeoutMillis, TimeUnit.MILLISECONDS);
                queue.taskDone();
                LOGGER.fine("Dequeued event of type: " + event.getClass().getSimpleName());

                boolean isFinal = isFinalEvent(event);
                subscriber.onNext(event);

                if (isFinal) {
                	LOGGER.fine("Stopping event consumption in consumeAll.");
                    queue.close(true);
                    subscriber.onComplete();
                    break;
                }
            } catch (IllegalStateException | InterruptedException e) {
            	continue;
            } catch (QueueClosedException | QueueEmptyException e) {
                if (queue.isClosed() || queue.isEmpty()) {
                	subscriber.onComplete();
                	break;
                }
            } catch (Exception e) {
	        	LOGGER.severe("Stopping event consumption due to an exception.");
	            exception = e;
	            continue;
	        } 
        }
	}
}
