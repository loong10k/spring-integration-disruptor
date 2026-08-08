package org.springframework.integration.disruptor;

import org.springframework.integration.Message;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;

/**
 * Holder class for {@link Message}s in the {@link RingBuffer}.
 * 
 * This is the default event type for the {@link MessageDrivenDisruptorWorkflow}.
 */
public final class MessagingEvent {

	private volatile Message<?> payload;

	/**
     * Returns the Spring Integration message payload held by this event.
     *
     * @return the message payload, or {@code null} if not yet set
     */
	public Message<?> getPayload() {
		return this.payload;
	}

	/**
     * Sets the Spring Integration message payload for this event.
     *
     * @param payload the message to store; may be {@code null}
     */
	public void setPayload(final Message<?> payload) {
		this.payload = payload;
	}

	/**
     * Creates a new {@link MessagingEventFactory} instance.
     *
     * @return a new event factory for {@link MessagingEvent} instances
     */
	public static EventFactory<MessagingEvent> newEventFactory() {
		return new MessagingEventFactory();
	}

}