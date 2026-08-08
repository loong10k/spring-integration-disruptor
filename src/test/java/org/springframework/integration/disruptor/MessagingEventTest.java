package org.springframework.integration.disruptor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import com.lmax.disruptor.EventFactory;

/**
 * Unit tests for {@link MessagingEvent}.
 */
class MessagingEventTest {

	@Test
	void shouldCreateViaFactoryMethod() {
		final EventFactory<MessagingEvent> factory = MessagingEvent.newEventFactory();
		assertNotNull(factory);
		final MessagingEvent event = factory.newInstance();
		assertNotNull(event);
		assertNull(event.getPayload());
	}

	@Test
	void shouldSetAndGetPayload() {
		final MessagingEvent event = new MessagingEvent();
		final Message<String> message = MessageBuilder.withPayload("test").build();
		event.setPayload(message);
		assertSame(message, event.getPayload());
	}

	@Test
	void shouldAllowNullPayload() {
		final MessagingEvent event = new MessagingEvent();
		event.setPayload(null);
		assertNull(event.getPayload());
	}

	@Test
	void shouldOverwritePayload() {
		final MessagingEvent event = new MessagingEvent();
		final Message<String> first = MessageBuilder.withPayload("first").build();
		final Message<String> second = MessageBuilder.withPayload("second").build();
		event.setPayload(first);
		event.setPayload(second);
		assertSame(second, event.getPayload());
	}
}
