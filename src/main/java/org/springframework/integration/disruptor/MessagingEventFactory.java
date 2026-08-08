package org.springframework.integration.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * {@link EventFactory} that produces {@link MessagingEvent}s.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessagingEvent
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