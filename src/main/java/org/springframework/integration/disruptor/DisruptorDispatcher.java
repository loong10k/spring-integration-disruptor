package org.springframework.integration.disruptor;

import java.util.List;

import org.springframework.integration.Message;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.dispatcher.AbstractDispatcher;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * A MessageDispatcher that publishes messages into a LMAX Disruptor ring buffer.
 * Registered MessageHandlers are invoked for each event processed from the ring buffer.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see DisruptorChannel
 * @see com.lmax.disruptor.dsl.Disruptor
 */
public class DisruptorDispatcher extends AbstractDispatcher {

	private final Disruptor<MessagingEvent> disruptor;

	/**
     * Constructs a dispatcher that registers an internal event handler with the given disruptor.
     *
     * @param disruptor the LMAX disruptor instance; must not be {@code null}
     */
	public DisruptorDispatcher(final Disruptor<MessagingEvent> disruptor) {
		this.disruptor = this.registerHandlerFor(disruptor);
	}

	@SuppressWarnings("unchecked")
	private Disruptor<MessagingEvent> registerHandlerFor(final Disruptor<MessagingEvent> disruptor) {
		
		disruptor.handleEventsWith(new EventHandler<MessagingEvent>() {

			public void onEvent(final MessagingEvent event, final long sequence, final boolean endOfBatch) throws Exception {
				final List<MessageHandler> handlers = DisruptorDispatcher.this.getHandlers();
				for (final MessageHandler handler : handlers) {
					handler.handleMessage(event.getPayload());
				}
			}

		});
		return disruptor;
	}

	/**
     * Dispatches a message by publishing it as an event into the disruptor ring buffer.
     *
     * @param message the message to dispatch; must not be {@code null}
     * @return always {@code true}, since ring buffer publication does not block
     */
	public boolean dispatch(final Message<?> message) {
		this.disruptor.publishEvent(new EventTranslator<MessagingEvent>() {

			public void translateTo(final MessagingEvent event, final long sequence) {
				event.setPayload(message);
			}

		});
		return true;
	}

	/**
     * Starts the underlying disruptor, activating all registered event handlers.
     */
	public void onInit() {
		this.disruptor.start();
	}

}
