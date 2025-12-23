/**
 * 
 */
package za.co.sindi.ai.a2a.server.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.types.Event;

/**
 * Event queue for A2A responses from agent.
 * <p>
 * Acts as a buffer between the agent's asynchronous execution and the
 * server's response handling (e.g., streaming via SSE). Supports tapping
 * to create child queues that receive the same events.
 *   
 * @author Buhake Sindi
 * @since 24 October 2025
 */
public class EventQueue implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(EventQueue.class.getName());
    private static final int DEFAULT_MAX_QUEUE_SIZE = 1024;

    private final BlockingQueue<Event> queue;
    private final List<EventQueue> children;
    private final ReentrantLock lock;
    private volatile boolean isClosed;

    /**
     * Exception thrown when attempting to dequeue from a closed empty queue.
     */
    public static class QueueClosedException extends Exception {
        public QueueClosedException(String message) {
            super(message);
        }
    }
    
    /**
     * Exception thrown when attempting to dequeue from an empty queue.
     */
    public static class QueueEmptyException extends Exception {
        public QueueEmptyException(String message) {
            super(message);
        }
    }

    /**
     * Initializes the EventQueue with default max queue size.
     */
    public EventQueue() {
        this(DEFAULT_MAX_QUEUE_SIZE);
    }

    /**
     * Initializes the EventQueue.
     *
     * @param maxQueueSize Maximum size of the queue. Must be greater than 0.
     * @throws IllegalArgumentException if maxQueueSize &lt;= 0.
     */
    public EventQueue(int maxQueueSize) {
        if (maxQueueSize <= 0) {
            throw new IllegalArgumentException("max_queue_size must be greater than 0");
        }

        this.queue = new LinkedBlockingQueue<>(maxQueueSize);
        this.children = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.isClosed = false;
        LOGGER.fine("EventQueue initialized.");
    }

    /**
     * Enqueues an event to this queue and all its children.
     *
     * @param event The event object to enqueue
     * @throws InterruptedException if interrupted while waiting to enqueue
     */
    public void enqueueEvent(Event event) throws InterruptedException {
        lock.lock();
        try {
            if (isClosed) {
                LOGGER.warning("Queue is closed. Event will not be enqueued.");
                return;
            }
        } finally {
            lock.unlock();
        }

        LOGGER.fine("Enqueuing event of type: " + event.getClass().getSimpleName());

        // Use put() to block if queue is full (consistent with Python's await put())
        queue.put(event);
        
        for (EventQueue child : children) {
            child.enqueueEvent(event);
        }
    }

    /**
     * Dequeues an event from the queue (blocking).
     *
     * @return The next event from the queue
     * @throws InterruptedException if interrupted while waiting
     * @throws QueueClosedException if the queue is closed
     * @throws QueueEmptyException if the queue is empty
     */
    public Event dequeueEvent() throws InterruptedException, QueueClosedException, QueueEmptyException {
        return dequeueEvent(false);
    }

    /**
     * Dequeues an event from the queue.
     *
     * @param noWait If true, retrieve an event immediately or throw IllegalStateException.
     *               If false, wait until an event is available.
     * @return The next event from the queue
     * @throws InterruptedException if interrupted while waiting
     * @throws QueueClosedException if the queue is closed and the queue is empty
     * @throws QueueEmptyException if the queue is empty
     */
    public Event dequeueEvent(boolean noWait) throws InterruptedException, QueueClosedException, QueueEmptyException {
        lock.lock();
        try {
            if (isClosed && queue.isEmpty()) {
                LOGGER.warning("Queue is closed. Event will not be dequeued.");
                throw new QueueClosedException("Queue is closed.");
            }
        } finally {
            lock.unlock();
        }

        if (noWait) {
            LOGGER.fine("Attempting to dequeue event (no_wait=true).");
            Event event = queue.poll();
            if (event == null) {
            	LOGGER.warning("Queue is empty. Event will not be dequeued.");
            	throw new QueueEmptyException("Queue is empty.");
            }
            LOGGER.fine("Dequeued event (noWait=true) of type: " + event.getClass().getSimpleName());
            return event;
        }

        LOGGER.fine("Attempting to dequeue event (waiting).");
        Event event = queue.take();
        LOGGER.fine("Dequeued event (waited) of type: " + event.getClass().getSimpleName());
        return event;
    }
    
    /**
     * Dequeues an event from the queue.
     *
     * @param timeout how long to wait before giving up, in units of
     *        {@code unit}
     * @param unit a {@code TimeUnit} determining how to interpret the
     *        {@code timeout} parameter
     * @return The next event from the queue
     * @throws InterruptedException if interrupted while waiting
     * @throws QueueClosedException if the queue is closed
     * @throws QueueEmptyException if the queue is empty
     */
    public Event dequeueEvent(long timeout, TimeUnit unit) throws InterruptedException, QueueClosedException, QueueEmptyException {
        lock.lock();
        try {
        	if (isClosed && queue.isEmpty()) {
                LOGGER.warning("Queue is closed. Event will not be dequeued.");
                throw new QueueClosedException("Queue is closed.");
            }
        } finally {
            lock.unlock();
        }

        if (timeout <= 0) {
            LOGGER.fine("Attempting to dequeue event.");
            Event event = queue.poll();
            if (event == null) {
            	LOGGER.warning("Queue is empty. Event will not be dequeued.");
            	throw new QueueEmptyException("Queue is empty.");
            }
            LOGGER.fine("Dequeued event (timeout=" + timeout + ") of type: " + event.getClass().getSimpleName());
            return event;
        }

        LOGGER.fine("Attempting to dequeue event (waiting).");
        Event event = queue.poll(timeout, unit);
        if (event == null) {
        	LOGGER.warning("Queue is empty. Event will not be dequeued.");
        	throw new QueueEmptyException("Queue is empty.");
        }
        LOGGER.fine("Dequeued event (waited) of type: " + event.getClass().getSimpleName());
        return event;
    }

    /**
     * Signals that a formerly enqueued task is complete.
     * <p>
     * Note: Java's BlockingQueue doesn't have a direct equivalent to Python's
     * task_done(). This is provided for API compatibility.
     */
    public void taskDone() {
        LOGGER.fine("Marking task as done in EventQueue.");
        // No-op in Java - BlockingQueue doesn't track task completion
    }

    /**
     * Taps the event queue to create a new child queue that receives all future events.
     *
     * @return A new EventQueue instance that will receive all events enqueued
     *         to this parent queue from this point forward
     */
    public EventQueue tap() {
        LOGGER.fine("Tapping EventQueue to create a child queue.");
        EventQueue child = new EventQueue();
        children.add(child);
        return child;
    }

    /**
     * Closes the queue for future push events (gracefully).
     * Waits for all queued events to be processed before closing.
     *
     * @throws InterruptedException if interrupted while waiting
     */
    public void close() throws InterruptedException {
        close(false);
    }

    /**
     * Closes the queue for future push events and also closes all child queues.
     *
     * @param immediate If true, immediately closes the queue and clears all unprocessed events.
     *                  If false, gracefully closes the queue, waiting for all queued events
     *                  to be processed.
     * @throws InterruptedException if interrupted while waiting
     */
    public void close(boolean immediate) throws InterruptedException {
        LOGGER.fine("Closing EventQueue.");
        
        lock.lock();
        try {
            if (isClosed && !immediate) return;
            isClosed = true;
            if (immediate) {
                clearEvents(true);
                for (EventQueue child : children) {
                    child.close(true);
                }
            } else {
                for (EventQueue child : children) {
                    child.close(false);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the queue is closed.
     *
     * @return true if the queue is closed, false otherwise
     */
    public boolean isClosed() {
        return isClosed;
    }
    
    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Clears all events from the current queue and optionally all child queues.
     *
     * @param clearChildQueues If true, clear all child queues as well.
     *                         If false, only clear the current queue.
     */
    public void clearEvents(boolean clearChildQueues) {
        LOGGER.fine("Clearing all events from EventQueue and child queues.");

        int clearedCount = 0;
        lock.lock();
        try {
            while (!queue.isEmpty()) {
                Event event = queue.poll();
                if (event != null) {
                    LOGGER.fine("Discarding unprocessed event of type: " + 
                               event.getClass().getSimpleName() + ", content: " + event);
                    clearedCount++;
                }
            }

            if (clearedCount > 0) {
                LOGGER.fine("Cleared " + clearedCount + " unprocessed events from EventQueue.");
            }
        } finally {
            lock.unlock();
        }

        // Clear all child queues
        if (clearChildQueues && !children.isEmpty()) {
            children.parallelStream().forEach(child -> child.clearEvents(true));
        }
    }

    /**
     * Clears all events from the current queue and all child queues.
     */
    public void clearEvents() {
        clearEvents(true);
    }
}