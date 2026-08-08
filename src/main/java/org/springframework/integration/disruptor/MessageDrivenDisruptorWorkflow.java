package org.springframework.integration.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.context.SmartLifecycle;
import org.springframework.integration.Message;
import org.springframework.integration.MessageDeliveryException;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.util.Assert;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;

/**
 * A concrete, final implementation of AbstractDisruptorWorkflow that implements
 * MessageHandler. Incoming messages from subscribed SubscribableChannels are published
 * into the underlying ring buffer. If publication fails, a MessageDeliveryException is thrown.
 * 
 * @param <T> the event type stored in the ring buffer
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractDisruptorWorkflow
 * @see org.springframework.integration.core.SubscribableChannel
 */
public final class MessageDrivenDisruptorWorkflow<T> extends AbstractDisruptorWorkflow<T> implements MessageHandler, SmartLifecycle {

	private final List<EventDrivenConsumer> eventDrivenConsumers;

	/**
     * Constructs a message-driven workflow with subscribable channels.
     *
     * @param ringBuffer            the LMAX ring buffer
     * @param executor              the executor for event processors
     * @param eventProcessors       the list of event processors
     * @param messageEventTranslator the translator for messages to events
     * @param subscribableChannels  the channels to subscribe to
     * @throws IllegalArgumentException if subscribableChannels is {@code null}
     */
	public MessageDrivenDisruptorWorkflow(final RingBuffer<T> ringBuffer, final Executor executor, final List<EventProcessor> eventProcessors,
			final MessageEventTranslator<T> messageEventTranslator, final List<SubscribableChannel> subscribableChannels) {
		super(ringBuffer, executor, eventProcessors, messageEventTranslator);
		Assert.isTrue(subscribableChannels != null, "SubscribableChannels can not be null");
		this.eventDrivenConsumers = toEventDrivenConsumers(subscribableChannels, this);
	}

	private static List<EventDrivenConsumer> toEventDrivenConsumers(final List<SubscribableChannel> subscribableChannels, final MessageHandler messageHandler) {
		final List<EventDrivenConsumer> eventDrivenConsumers = new ArrayList<EventDrivenConsumer>();
		for (final SubscribableChannel subscribableChannel : subscribableChannels) {
			eventDrivenConsumers.add(new EventDrivenConsumer(subscribableChannel, messageHandler));
		}
		return eventDrivenConsumers;
	}

	/**
     * Handles an incoming message by publishing it into the ring buffer.
     *
     * @param message the message to handle
     * @throws MessageDeliveryException if publication to the ring buffer fails
     */
	public void handleMessage(final Message<?> message) throws MessagingException {
		final boolean sent = this.publish(message);
		if (!sent) {
			throw new MessageDeliveryException(message);
		}
	}

	@Override
	public void doStart() {
		this.startEventDrivenConsumers();
	}

	@Override
	public void doStop() {
		this.stopEventDrivenConsumers();
	}

	private void startEventDrivenConsumers() {
		for (final EventDrivenConsumer eventDrivenConsumer : this.eventDrivenConsumers) {
			eventDrivenConsumer.start();
		}
	}

	private void stopEventDrivenConsumers() {
		for (final EventDrivenConsumer eventDrivenConsumer : this.eventDrivenConsumers) {
			eventDrivenConsumer.stop();
		}
	}

}
