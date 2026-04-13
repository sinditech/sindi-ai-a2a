/**
 * 
 */
package za.co.sindi.ai.a2a.server.requesthandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.logging.Level;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.server.A2AServerError;
import za.co.sindi.ai.a2a.server.ServerCallContext;
import za.co.sindi.ai.a2a.server.agentexecution.AgentExecutor;
import za.co.sindi.ai.a2a.server.agentexecution.RequestContext;
import za.co.sindi.ai.a2a.server.agentexecution.RequestContext.RequestContextBuilder;
import za.co.sindi.ai.a2a.server.agentexecution.SimpleRequestContextBuilder;
import za.co.sindi.ai.a2a.server.events.EventConsumer;
import za.co.sindi.ai.a2a.server.events.EventQueue;
import za.co.sindi.ai.a2a.server.events.InMemoryQueueManager;
import za.co.sindi.ai.a2a.server.events.QueueManager;
import za.co.sindi.ai.a2a.server.tasks.PushNotificationConfigStore;
import za.co.sindi.ai.a2a.server.tasks.PushNotificationSender;
import za.co.sindi.ai.a2a.server.tasks.ResultAggregator;
import za.co.sindi.ai.a2a.server.tasks.ResultAggregator.EventInterrupt;
import za.co.sindi.ai.a2a.server.tasks.TaskManager;
import za.co.sindi.ai.a2a.server.tasks.TaskStore;
import za.co.sindi.ai.a2a.types.DeleteTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.Event;
import za.co.sindi.ai.a2a.types.GetTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.InternalError;
import za.co.sindi.ai.a2a.types.InvalidParamsError;
import za.co.sindi.ai.a2a.types.Kind;
import za.co.sindi.ai.a2a.types.ListTaskPushNotificationConfigParams;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.PushNotificationConfig;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskIdParams;
import za.co.sindi.ai.a2a.types.TaskNotCancelableError;
import za.co.sindi.ai.a2a.types.TaskNotFoundError;
import za.co.sindi.ai.a2a.types.TaskPushNotificationConfig;
import za.co.sindi.ai.a2a.types.TaskQueryParams;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.UnsupportedOperationError;
import za.co.sindi.ai.a2a.utils.Tasks;
import za.co.sindi.commons.concurrent.TransformingPublisher;

/**
 * Default request handler for all incoming requests.<p />
 * 
 * This handler provides default implementations for all A2A JSON-RPC methods,
 *  coordinating between the {@link AgentExecutor}, {@link TaskStore}, {@link QueueManager},
 * and optional `PushNotifier`.
 * 
 * @author Buhake Sindi
 * @since 25 October 2025
 */
public class DefaultRequestHandler implements RequestHandler {
	private static final Logger LOGGER = Logger.getLogger(DefaultRequestHandler.class.getName());
	
	private static final Set<TaskState> TERMINAL_TASK_STATES = Set.of(TaskState.COMPLETED, TaskState.CANCELED, TaskState.FAILED, TaskState.REJECTED);
	
	private final AgentExecutor agentExecutor;
	private final TaskStore taskStore;
	private final QueueManager queueManager;
	private final PushNotificationConfigStore pushConfigStore;
	private final PushNotificationSender pushSender;
	private final RequestContextBuilder requestContextBuilder;
	private final Map<String, CompletableFuture<Void>> runningAgents;
	private final Set<CompletableFuture<Void>> backgroundTasks;
	private ExecutorService executor;
	
	/**
	 * Initializes the DefaultRequestHandler.
	 * 
	 * @param agentExecutor The {@link AgentExecutor} instance to run agent logic.
	 * @param taskStore The {@link TaskStore} instance to manage task persistence.
	 */
	public DefaultRequestHandler(AgentExecutor agentExecutor, TaskStore taskStore) {
		this(agentExecutor, taskStore, null, null, null, null, Executors.newVirtualThreadPerTaskExecutor());
	}
	
	/**
	 * Initializes the DefaultRequestHandler.
	 * 
	 * @param agentExecutor The {@link AgentExecutor} instance to run agent logic.
	 * @param taskStore The {@link TaskStore} instance to manage task persistence.
	 * @param executor
	 */
	public DefaultRequestHandler(AgentExecutor agentExecutor, TaskStore taskStore, ExecutorService executor) {
		this(agentExecutor, taskStore, null, null, null, null, executor);
	}
	
	/**
	 * Initializes the DefaultRequestHandler.
	 * 
	 * @param agentExecutor The {@link AgentExecutor} instance to run agent logic.
	 * @param taskStore  The {@link TaskStore} instance to manage task persistence.
	 * @param queueManager The {@link QueueManager} instance to manage event queues. Defaults to {@link InMemoryQueueManager}.
	 * @param pushConfigStore The {@link PushNotificationConfigStore} instance for managing push notification configurations. Defaults to <code>null</code>.
	 * @param pushSender The {@link PushNotificationSender} instance for sending push notifications. Defaults to <code>null</code>.
	 * @param requestContextBuilder The {@link RequestContextBuilder} instance used to build request contexts. Defaults to {@link SimpleRequestContextBuilder}.
	 */
	public DefaultRequestHandler(AgentExecutor agentExecutor, TaskStore taskStore, QueueManager queueManager,
			PushNotificationConfigStore pushConfigStore, PushNotificationSender pushSender,
			RequestContextBuilder requestContextBuilder) {
		this(agentExecutor, taskStore, queueManager, pushConfigStore, pushSender, requestContextBuilder, Executors.newVirtualThreadPerTaskExecutor());
	}

	/**
	 * Initializes the DefaultRequestHandler.
	 * 
	 * @param agentExecutor The {@link AgentExecutor} instance to run agent logic.
	 * @param taskStore  The {@link TaskStore} instance to manage task persistence.
	 * @param queueManager The {@link QueueManager} instance to manage event queues. Defaults to {@link InMemoryQueueManager}.
	 * @param pushConfigStore The {@link PushNotificationConfigStore} instance for managing push notification configurations. Defaults to <code>null</code>.
	 * @param pushSender The {@link PushNotificationSender} instance for sending push notifications. Defaults to <code>null</code>.
	 * @param requestContextBuilder The {@link RequestContextBuilder} instance used to build request contexts. Defaults to {@link SimpleRequestContextBuilder}.
	 * @param executor
	 */
	public DefaultRequestHandler(AgentExecutor agentExecutor, TaskStore taskStore, QueueManager queueManager,
			PushNotificationConfigStore pushConfigStore, PushNotificationSender pushSender,
			RequestContextBuilder requestContextBuilder, ExecutorService executor) {
		super();
		this.agentExecutor = Objects.requireNonNull(agentExecutor, "An Agent Executor is required.");
		this.taskStore = Objects.requireNonNull(taskStore, "A Task store is required.");
		this.queueManager = (queueManager == null ? new InMemoryQueueManager() : queueManager);
		this.pushConfigStore = pushConfigStore;
		this.pushSender = pushSender;
		this.requestContextBuilder = (requestContextBuilder == null ? new SimpleRequestContextBuilder(taskStore) : requestContextBuilder);
		this.runningAgents = new ConcurrentHashMap<>();
		this.backgroundTasks = ConcurrentHashMap.newKeySet();
		this.executor = executor;
	}

	/**
	 * Default handler for 'tasks/get'.
	 */
	@Override
	public Task onGetTask(TaskQueryParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		Task task = taskStore.load(params.id(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		return Tasks.applyHistoryLength(task, params.historyLength());
	}

	/**
	 * Default handler for 'tasks/cancel'.
	 */
	@Override
	public Task onCancelTask(TaskIdParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		Task task = taskStore.load(params.id(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		if (TERMINAL_TASK_STATES.contains(task.getStatus().state())) {
			throw new A2AServerError(new TaskNotCancelableError("Task cannot be canceled - current state: " + task.getStatus().state()));
		}
		
		TaskManager taskManager = new TaskManager(task.getId(), task.getContextId(), taskStore, null, context);
		ResultAggregator resultAggregator = new ResultAggregator(taskManager);
		EventQueue queue = queueManager.tap(task.getId());
		if (queue == null) queue = new EventQueue();
		agentExecutor.cancel(new RequestContext(null, task, null, task.getId(), task.getContextId(), context), queue);
		Optional.ofNullable(runningAgents.get(task.getId())).ifPresent(producerTask -> producerTask.cancel(true));
		EventConsumer consumer = new EventConsumer(queue);
		Event result = resultAggregator.consumeAll(consumer);
		if (!(result instanceof Task resultTask)) {
			throw new A2AServerError(new InternalError("Agent did not return valid response for cancel."));
		}
		
		if (resultTask.getStatus().state() != TaskState.CANCELED) {
			throw new A2AServerError(new TaskNotCancelableError("Task cannot be canceled - current state: " + resultTask.getStatus().state()));
		}
		
		return resultTask;
	}

	/**
	 * Default handler for 'message/send' interface (non-streaming).
	 */
	@Override
	public Event onMessageSend(MessageSendParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		MessageExecution execution = setupMessageExecution(params, context);
		EventConsumer consumer = new EventConsumer(execution.queue());
		CompletableFuture<Void> producerTask = execution.producerTask();
		producerTask.whenComplete((_, _) -> consumer.agentTaskCallback(producerTask));
		
		boolean blocking = true;
		if (params.configuration() != null && params.configuration().blocking() != null && Boolean.FALSE.equals(params.configuration().blocking())) blocking = false;
		
		EventInterrupt eventInterrupt = null;
		try {
			producerTask.get();
			Runnable pushNotificationCallback = () -> sendPushNotificationIfNeeded(execution.taskId(), execution.resultAggregator());
			eventInterrupt = execution.resultAggregator().consumeAndBreakOnInterrupt(consumer, blocking, pushNotificationCallback);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Agent Execution failed.");
			producerTask.cancel(true);
			throw new A2AServerError(new InternalError(e.getLocalizedMessage()));
		} finally {
			if (eventInterrupt != null && eventInterrupt.interrupted())  {
				CompletableFuture<Void> cleanupTask = cleanupProducer(execution.taskId(), producerTask);
//				cleanupTask.setName("cleanup_producer:" + execution.taskId());
				trackBackgroundTask(cleanupTask);
//				executor.submit(cleanupTask);
				cleanupTask.join();
			} else cleanupProducer(execution.taskId(), producerTask).join();
		}
		
		Kind result = eventInterrupt == null ? null : eventInterrupt.result();
		if (result == null) {
			throw new A2AServerError(new InternalError());
		}
		
		if (result instanceof Task task) {
			validateTaskIdMatch(execution.taskId(), task.getId());
			if (params.configuration() != null) {
				result = Tasks.applyHistoryLength(task, params.configuration().historyLength());
			}
		}
		
		sendPushNotificationIfNeeded(execution.taskId(), execution.resultAggregator());
		return (Event) result;
	}

	/**
	 * Default handler for 'message/stream' (streaming).
	 */
	@Override
	public Publisher<Event> onMessageSendStream(MessageSendParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		MessageExecution execution = setupMessageExecution(params, context);
		EventConsumer consumer = new EventConsumer(execution.queue());
		CompletableFuture<Void> producerTask = execution.producerTask();
		producerTask.whenComplete((_, _) -> consumer.agentTaskCallback(producerTask));
		
		Publisher<Event> results = null;
		try {
			producerTask.join();
			
			results = execution.resultAggregator().consumeAndEmit(consumer);
			results = new TransformingPublisher<Event, Event>(results, (event) -> {
				if (event instanceof Task task) validateTaskIdMatch(execution.taskId(), task.getId());

				sendPushNotificationIfNeeded(execution.taskId(), execution.resultAggregator());
				
				return event;
			});
		} catch(CancellationException e) {
			CompletableFuture<Void> backgroundTask = CompletableFuture.runAsync(() -> {
				execution.resultAggregator.consumeAll(consumer);
			}, executor);
//			backgroundTask.setName("background_consume:" + execution.taskId());
			trackBackgroundTask(backgroundTask);
//			executor.submit(backgroundTask);
			backgroundTask.join();
		} finally {
			CompletableFuture<Void> cleanupTask = cleanupProducer(execution.taskId(), producerTask);
//			cleanupTask.setName("cleanup_producer:" + execution.taskId());
			trackBackgroundTask(cleanupTask);
			cleanupTask.join();
		}
		
		return results;
	}
	
	private void validateTaskIdMatch(final String taskId, final String eventTaskId) {
		if (!taskId.equals(eventTaskId)) {
			LOGGER.severe(String.format("Agent generated taskId=%s does not match the RequestContext taskId=%s.", eventTaskId, taskId));
			throw new A2AServerError(new InternalError("Task ID mismatch in agent response"));
		}
	}
	
	/**
	 * Sends push notification if configured and task is available.
	 * @param taskId
	 * @param resultAggregator
	 */
	private void sendPushNotificationIfNeeded(final String taskId, final ResultAggregator resultAggregator) {
		if (pushSender != null && taskId != null) {
			Kind kind = resultAggregator.getCurrentResult();
			if (kind instanceof Task task) pushSender.sendNotification(task);
		}
	}
	
	/**
	 * Runs the agent's `execute` method and closes the queue afterwards.
	 * 
	 * @param request The request context for the agent.
	 * @param queue The event queue for the agent to publish to.
	 * @return
	 */
	private CompletableFuture<Void> runEventStream(final RequestContext request, final EventQueue queue) {
		return CompletableFuture.runAsync(() -> {
			agentExecutor.execute(request, queue);
		}, executor).whenComplete((_, _) -> {
			try {
				queue.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Thread.currentThread().interrupt();
			}
		});
	}
	
	private MessageExecution setupMessageExecution(MessageSendParams params, ServerCallContext context) {
		TaskManager taskManager = new TaskManager(params.message().getTaskId(), params.message().getContextId(), taskStore, params.message(), context);
		Task task = taskManager.getTask();
		if (task != null) {
			if (TERMINAL_TASK_STATES.contains(task.getStatus().state())) {
				throw new A2AServerError(new InvalidParamsError("Task " + task.getId() + " is in terminal state: " + task.getStatus().state()));
			}
			task = taskManager.updateWithMessage(params.message(), task);
		} else if (params.message().getTaskId() != null) {
			throw new A2AServerError(new TaskNotFoundError("Task " + params.message().getTaskId() + " was specified but does not exist."));
		}
		
		RequestContext requestContext = requestContextBuilder.params(params)
															.taskId(task == null ? null : task.getId())
															.contextId(params.message().getContextId())
															.task(task)
															.callContext(context)
															.build();
		
		String taskId = requestContext.getTaskId();
		
		if (pushConfigStore != null && params.configuration() != null && params.configuration().pushNotificationConfig() != null) {
			pushConfigStore.setInfo(taskId, params.configuration().pushNotificationConfig());
		}
		
		EventQueue queue = queueManager.createOrTap(taskId);
		ResultAggregator resultAggregator = new ResultAggregator(taskManager);
		CompletableFuture<Void> producerTask = runEventStream(requestContext, queue);
		
		registerProducer(taskId, producerTask);
		
		return new MessageExecution(taskManager, taskId, queue, resultAggregator, producerTask);
	}
	
	/**
	 * "Registers the agent execution task with the handler.
	 * 
	 * @param taskId
	 * @param producerTask
	 */
	private void registerProducer(final String taskId, final CompletableFuture<Void> producerTask) {
		runningAgents.put(taskId, producerTask);
	}
	
	/**
	 * Cleans up the agent execution task and queue manager entry.
	 * 
	 * @param taskId
	 * @param producerTask
	 */
	private CompletableFuture<Void> cleanupProducer(final String taskId, final CompletableFuture<Void> producerTask) {
		
		return producerTask.exceptionally(exception -> {
			if (exception instanceof CancellationException) {
				LOGGER.fine(String.format("Producer task %s was cancelled during cleanup.", taskId));
			}
			return null;
		}).whenComplete((_, _) -> { 
			queueManager.close(taskId);
			runningAgents.remove(taskId);
		});
	}
	
	/**
	 * Tracks a background task and logs exceptions on completion.
	 * 
     * This avoids unreferenced tasks (and associated lint warnings) while
     * ensuring any exceptions are surfaced in logs.
	 * @param task
	 */
	private void trackBackgroundTask(final CompletableFuture<Void> task) {
		backgroundTasks.add(task);
		task.whenComplete((_, exception) -> {
			var cancelled = (exception != null && exception instanceof CancellationException) || task.isCancelled();
			if (cancelled) {
				LOGGER.fine("Background task cancelled.");
			} else if (exception != null) {
				LOGGER.log(Level.SEVERE, "Background task failed.", exception);
			}
			
			backgroundTasks.remove(task);
		});
	}

	/**
	 * Default handler for 'tasks/pushNotificationConfig/set'.
	 */
	@Override
	public TaskPushNotificationConfig onSetTaskPushNotificationConfig(TaskPushNotificationConfig params,
			ServerCallContext context) {
		// TODO Auto-generated method stub
		if (pushConfigStore == null) {
			throw new A2AServerError(new UnsupportedOperationError());
		}
		
		Task task = taskStore.load(params.taskId(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		pushConfigStore.setInfo(params.taskId(), params.pushNotificationConfig());
		return params;
	}

	/**
	 * Default handler for 'tasks/pushNotificationConfig/get'.
	 */
	@Override
	public TaskPushNotificationConfig onGetTaskPushNotificationConfig(TaskIdParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		return onGetTaskPushNotificationConfig(params.id(), context);
	}

	/**
	 * Default handler for 'tasks/pushNotificationConfig/get'.
	 */
	@Override
	public TaskPushNotificationConfig onGetTaskPushNotificationConfig(GetTaskPushNotificationConfigParams params,
			ServerCallContext context) {
		// TODO Auto-generated method stub
		return onGetTaskPushNotificationConfig(params.id(), context);
	}
	
	/**
	 * Default handler for 'tasks/pushNotificationConfig/get'.
	 */
	private TaskPushNotificationConfig onGetTaskPushNotificationConfig(final String paramsId, ServerCallContext context) {
		// TODO Auto-generated method stub
		if (pushConfigStore == null) {
			throw new A2AServerError(new UnsupportedOperationError());
		}
		
		Task task = taskStore.load(paramsId, context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		List<PushNotificationConfig> pushNotificationConfigList = pushConfigStore.getInfo(paramsId);
		if (pushNotificationConfigList == null || pushNotificationConfigList.isEmpty()) {
			throw new A2AServerError(new InternalError("Push notification config not found."));
		}
		return new TaskPushNotificationConfig(paramsId, pushNotificationConfigList.get(0));
	}

	/**
	 * "Default handler for 'tasks/resubscribe'.
	 */
	@Override
	public Publisher<Event> onResubmitToTask(TaskIdParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		Task task = taskStore.load(params.id(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		if (TERMINAL_TASK_STATES.contains(task.getStatus().state())) {
			throw new A2AServerError(new TaskNotCancelableError("Task " + task.getId() + " is in terminal state: " + task.getStatus().state()));
		}
		
		TaskManager taskManager = new TaskManager(task.getId(), task.getContextId(), taskStore, null, context);
		ResultAggregator resultAggregator = new ResultAggregator(taskManager);
		EventQueue queue = queueManager.tap(task.getId());
		if (queue == null) throw new A2AServerError(new TaskNotFoundError());
		EventConsumer consumer = new EventConsumer(queue);
		return resultAggregator.consumeAndEmit(consumer);
	}

	/**
	 * Default handler for 'tasks/pushNotificationConfig/list'.
	 */
	@Override
	public List<TaskPushNotificationConfig> onListTaskPushNotificationConfig(
			ListTaskPushNotificationConfigParams params, ServerCallContext context) {
		// TODO Auto-generated method stub
		if (pushConfigStore == null) {
			throw new A2AServerError(new UnsupportedOperationError());
		}
		
		Task task = taskStore.load(params.id(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		List<PushNotificationConfig> pushNotificationConfigList = pushConfigStore.getInfo(params.id());
        List<TaskPushNotificationConfig> taskPushNotificationConfigList = new ArrayList<>();
        if (pushNotificationConfigList != null) {
            for (PushNotificationConfig pushNotificationConfig : pushNotificationConfigList) {
                taskPushNotificationConfigList.add(new TaskPushNotificationConfig(params.id(), pushNotificationConfig));
            }
        }
        return taskPushNotificationConfigList;
	}

	/**
	 * "Default handler for 'tasks/pushNotificationConfig/delete'.
	 */
	@Override
	public void onDeleteTaskPushNotificationConfig(DeleteTaskPushNotificationConfigParams params,
			ServerCallContext context) {
		// TODO Auto-generated method stub
		if (pushConfigStore == null) {
			throw new A2AServerError(new UnsupportedOperationError());
		}
		
		Task task = taskStore.load(params.id(), context);
		if (task == null) {
			throw new A2AServerError(new TaskNotFoundError());
		}
		
		pushConfigStore.deleteInfo(params.id(), params.pushNotificationConfigId());
	}
	
	private final record MessageExecution(TaskManager taskManager, String taskId, EventQueue queue, ResultAggregator resultAggregator, CompletableFuture<Void> producerTask) {}
}
