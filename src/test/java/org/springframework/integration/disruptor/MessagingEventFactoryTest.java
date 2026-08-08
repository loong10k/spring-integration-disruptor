package org.springframework.integration.disruptor;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link MessagingEventFactory}.
 */
class MessagingEventFactoryTest {

	@Test
	void shouldCreateNewMessagingEvent() {
		final MessagingEventFactory factory = new MessagingEventFactory();
		final MessagingEvent event = factory.newInstance();
		assertNotNull(event);
		assertNull(event.getPayload());
	}

	@Test
	void shouldCreateDistinctInstances() {
		final MessagingEventFactory factory = new MessagingEventFactory();
		final MessagingEvent first = factory.newInstance();
		final MessagingEvent second = factory.newInstance();
		assertNotSame(first, second);
	}
}
