package org.springframework.integration.disruptor;

import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.dispatcher.MessageDispatcher;

import com.lmax.disruptor.dsl.Disruptor;

/**
 * A Spring Integration MessageChannel backed by a LMAX Disruptor ring buffer.
 * Messages sent to this channel are published into the disruptor and dispatched
 * to registered handlers via the internal DisruptorDispatcher.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see DisruptorDispatcher
 * @see com.lmax.disruptor.dsl.Disruptor
 */
public class DisruptorChannel extends AbstractSubscribableChannel {

	private final DisruptorDispatcher dispatcher;

	/**
     * Constructs a new channel backed by the given disruptor.
     *
     * @param disruptor the LMAX disruptor instance; must not be {@code null}
     */
	public DisruptorChannel(final Disruptor<MessagingEvent> disruptor) {
		this.dispatcher = new DisruptorDispatcher(disruptor);
	}

	@Override
	/**
     * Returns the internal disruptor-based message dispatcher.
     *
     * @return the dispatcher used to publish messages into the ring buffer
     */
	protected MessageDispatcher getDispatcher() {
		return this.dispatcher;
	}

	@Override
	/**
     * Initializes the dispatcher by starting the underlying disruptor.
     *
     * @throws Exception if initialization fails
     */
	protected void onInit() throws Exception {
		this.dispatcher.onInit();
	}

}
