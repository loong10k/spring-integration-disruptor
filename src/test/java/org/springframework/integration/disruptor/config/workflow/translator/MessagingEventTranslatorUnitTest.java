package org.springframework.integration.disruptor.config.workflow.translator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.integration.Message;
import org.springframework.integration.disruptor.MessagingEvent;
import org.springframework.integration.support.MessageBuilder;

/**
 * Unit tests for {@link MessagingEventTranslator}.
 */
public class MessagingEventTranslatorUnitTest {

	@Test
	public void shouldTranslateMessageToMessagingEvent() {
		final MessagingEventTranslator translator = new MessagingEventTranslator();
		final Message<String> message = MessageBuilder.withPayload("hello").build();
		final MessagingEvent event = new MessagingEvent();

		translator.translateTo(message, event);
		assertSame(message, event.getPayload());
	}

	@Test
	public void shouldTranslateNullMessage() {
		final MessagingEventTranslator translator = new MessagingEventTranslator();
		final MessagingEvent event = new MessagingEvent();

		translator.translateTo(null, event);
		assertNull(event.getPayload());
	}

	@Test
	public void shouldOverwritePreviousPayload() {
		final MessagingEventTranslator translator = new MessagingEventTranslator();
		final Message<String> first = MessageBuilder.withPayload("first").build();
		final Message<String> second = MessageBuilder.withPayload("second").build();
		final MessagingEvent event = new MessagingEvent();

		translator.translateTo(first, event);
		assertSame(first, event.getPayload());

		translator.translateTo(second, event);
		assertSame(second, event.getPayload());
	}
}
