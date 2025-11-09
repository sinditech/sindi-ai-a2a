package za.co.sindi.ai.a2a.client.transports;

import java.util.function.Consumer;
import java.util.function.Function;

import za.co.sindi.commons.net.sse.Event;
import za.co.sindi.commons.net.sse.EventHandler;

class ConsumingEventHandler<T> implements EventHandler {
	private final Function<Event, T> mapper;
	private final Consumer<T> dataConsumer;
	private final Consumer<Throwable> errorConsumer;
	
	/**
	 * @param mapper
	 * @param dataConsumer
	 * @param errorConsumer
	 */
	public ConsumingEventHandler(Function<Event, T> mapper, Consumer<T> dataConsumer,
			Consumer<Throwable> errorConsumer) {
		super();
		this.mapper = mapper;
		this.dataConsumer = dataConsumer;
		this.errorConsumer = errorConsumer;
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		dataConsumer.accept(mapper.apply(event));
	}

	@Override
	public void onError(Throwable error) {
		// TODO Auto-generated method stub
		errorConsumer.accept(error);
	}
}
