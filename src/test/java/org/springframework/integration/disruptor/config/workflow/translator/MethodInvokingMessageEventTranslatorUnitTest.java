package org.springframework.integration.disruptor.config.workflow.translator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.integration.Message;
import org.springframework.integration.disruptor.config.annotation.EventTranslator;
import org.springframework.integration.support.MessageBuilder;

/**
 * Unit tests for {@link MethodInvokingMessageEventTranslator}.
 */
public class MethodInvokingMessageEventTranslatorUnitTest {

	@Test
	public void shouldTranslateViaAnnotatedMethod() {
		final TranslatorTarget target = new TranslatorTarget();
		final MethodInvokingMessageEventTranslator<TestEvent> translator =
				new MethodInvokingMessageEventTranslator<TestEvent>(target, TestEvent.class);
		final Message<String> message = MessageBuilder.withPayload("hello").build();
		final TestEvent event = new TestEvent();

		translator.translateTo(message, event);
		assertSame(message, event.lastMessage);
	}

	@Test
	public void shouldTranslateViaCompatibleMethod() {
		final SimpleTranslatorTarget target = new SimpleTranslatorTarget();
		final MethodInvokingMessageEventTranslator<TestEvent> translator =
				new MethodInvokingMessageEventTranslator<TestEvent>(target, TestEvent.class);
		final Message<String> message = MessageBuilder.withPayload("world").build();
		final TestEvent event = new TestEvent();

		translator.translateTo(message, event);
		assertSame(message, event.lastMessage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenNoSuitableMethod() {
		new MethodInvokingMessageEventTranslator<TestEvent>(new Object(), TestEvent.class);
	}

	public static class TestEvent {
		public Message<?> lastMessage;
	}

	public static class TranslatorTarget {
		@EventTranslator
		public void translate(Message<?> message, TestEvent event) {
			event.lastMessage = message;
		}
	}

	public static class SimpleTranslatorTarget {
		public void translate(Message<?> message, TestEvent event) {
			event.lastMessage = message;
		}
	}
}
