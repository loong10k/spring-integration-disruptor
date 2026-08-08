package org.springframework.integration.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * {@link EventFactory} that produces {@link MessagingEvent}s.
 */
public final class MessagingEventFactory implements EventFactory<MessagingEvent> {

	/**
     * Creates a new {@link MessagingEvent} instance.
     *
     * @return a new, empty messaging event
     */
	public MessagingEvent newInstance() {
		return new MessagingEvent();
	}

}