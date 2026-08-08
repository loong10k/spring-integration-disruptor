package org.springframework.integration.disruptor.config;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * Unit tests for {@link DisruptorNamespaceElements}.
 */
public class DisruptorNamespaceElementsTest {

	@Test
	public void shouldDefineElementConstants() {
		assertEquals("disruptor", DisruptorNamespaceElements.ELEMENT_DISRUPTOR);
		assertEquals("ring-buffer", DisruptorNamespaceElements.ELEMENT_RING_BUFFER);
		assertEquals("channel", DisruptorNamespaceElements.ELEMENT_CHANNEL);
		assertEquals("messaging-event-factory", DisruptorNamespaceElements.ELEMENT_MESSAGING_EVENT_FACTORY);
		assertEquals("message-driven-workflow", DisruptorNamespaceElements.ELEMENT_MESSAGE_DRIVEN_WORKFLOW);
		assertEquals("workflow", DisruptorNamespaceElements.ELEMENT_WORKFLOW);
		assertEquals("forwarding-event-handler", DisruptorNamespaceElements.ELEMENT_FORWARDING_EVENT_HANDLER);
	}

	@Test
	public void shouldDefineRingBufferAttributeConstants() {
		assertEquals("event-factory", DisruptorNamespaceElements.RING_BUFFER_ATTRIBUTE_EVENT_FACTORY);
		assertEquals("claim-strategy", DisruptorNamespaceElements.RING_BUFFER_ATTRIBUTE_CLAIM_STRATEGY);
		assertEquals("wait-strategy", DisruptorNamespaceElements.RING_BUFFER_ATTRIBUTE_WAIT_STRATEGY);
		assertEquals("buffer-size", DisruptorNamespaceElements.RING_BUFFER_ATTRIBUTE_BUFFER_SIZE);
	}

	@Test
	public void shouldDefineDisruptorAttributeConstants() {
		assertEquals("executor", DisruptorNamespaceElements.DISRUPTOR_ATTRIBUTE_EXECUTOR);
	}

	@Test
	public void shouldDefineChannelAttributeConstants() {
		assertEquals("disruptor", DisruptorNamespaceElements.CHANNEL_ATTRIBUTE_DISRUPTOR);
	}

	@Test
	public void shouldPreventInstantiation() throws Exception {
		final Constructor<DisruptorNamespaceElements> ctor = DisruptorNamespaceElements.class.getDeclaredConstructor();
		ctor.setAccessible(true);
		try {
			ctor.newInstance();
			fail("Expected InvocationTargetException wrapping IllegalStateException");
		} catch (final InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalStateException);
		}
	}
}
