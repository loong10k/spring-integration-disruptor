package org.springframework.integration.disruptor.config.workflow.translator;

import org.springframework.integration.Message;
import org.springframework.integration.disruptor.MessagingEvent;

/**
 * Default MessageEventTranslator implementation that sets the Spring Integration
 * message as the payload of a MessagingEvent.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessageEventTranslator
 * @see org.springframework.integration.disruptor.MessagingEvent
 */
public class MessagingEventTranslator implements MessageEventTranslator<MessagingEvent> {

	/**
     * Translates a Spring Integration message into a MessagingEvent by setting the payload.
     *
     * @param message the source message
     * @param event   the target messaging event
     */
	public void translateTo(final Message<?> message, final MessagingEvent event) {
		event.setPayload(message);
	}

}
