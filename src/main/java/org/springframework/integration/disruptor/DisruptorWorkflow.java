package org.springframework.integration.disruptor;

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;

/**
 * A concrete, final implementation of AbstractDisruptorWorkflow that also implements
 * MessageChannel. Messages sent via send() are published into the underlying ring buffer.
 * 
 * @param <T> the event type stored in the ring buffer
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractDisruptorWorkflow
 */
public final class DisruptorWorkflow<T> extends AbstractDisruptorWorkflow<T> implements MessageChannel {

	/**
     * Constructs a new disruptor workflow with the given components.
     *
     * @param ringBuffer            the LMAX ring buffer
     * @param executor              the executor for event processors
     * @param eventProcessors       the list of event processors
     * @param messageEventTranslator the translator for messages to events
     */
	public DisruptorWorkflow(final RingBuffer<T> ringBuffer, final Executor executor, final List<EventProcessor> eventProcessors,
			final MessageEventTranslator<T> messageEventTranslator) {
		super(ringBuffer, executor, eventProcessors, messageEventTranslator);
	}

	/**
     * Sends a message by publishing it into the underlying ring buffer.
     *
     * @param message the message to send
     * @return {@code true} if the message was successfully published
     */
	public boolean send(final Message<?> message) {
		return this.publish(message);
	}

	/**
     * Sends a message, ignoring the timeout parameter.
     *
     * @param message the message to send
     * @param timeout the timeout value (ignored)
     * @return {@code true} if the message was successfully published
     */
	public boolean send(final Message<?> message, final long timeout) {
		this.logger.warn("Timeout is ignored in DisruptorWorkflow.");
		return this.send(message);
	}
}
