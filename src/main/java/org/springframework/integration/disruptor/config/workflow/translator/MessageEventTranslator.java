package org.springframework.integration.disruptor.config.workflow.translator;

import org.springframework.integration.Message;

/**
 * Strategy interface for translating a Spring Integration Message into a
 * ring buffer event of type T.
 * 
 * @param <T> the event type stored in the ring buffer
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessagingEventTranslator
 * @see MethodInvokingMessageEventTranslator
 */
public interface MessageEventTranslator<T> {

	void translateTo(Message<?> message, T event);

}