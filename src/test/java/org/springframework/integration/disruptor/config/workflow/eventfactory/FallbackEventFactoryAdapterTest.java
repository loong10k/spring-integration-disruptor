package org.springframework.integration.disruptor.config.workflow.eventfactory;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link FallbackEventFactoryAdapter}.
 */
public class FallbackEventFactoryAdapterTest {

	@Test
	public void shouldCreateInstance() {
		final FallbackEventFactoryAdapter<SimpleEvent> factory =
				new FallbackEventFactoryAdapter<SimpleEvent>(SimpleEvent.class);
		final SimpleEvent event = factory.newInstance();
		assertNotNull(event);
	}

	@Test
	public void shouldCreateDistinctInstances() {
		final FallbackEventFactoryAdapter<SimpleEvent> factory =
				new FallbackEventFactoryAdapter<SimpleEvent>(SimpleEvent.class);
		final SimpleEvent first = factory.newInstance();
		final SimpleEvent second = factory.newInstance();
		assertNotSame(first, second);
	}

	@Test(expected = org.springframework.beans.BeanInstantiationException.class)
	public void shouldThrowForAbstractType() {
		new FallbackEventFactoryAdapter<AbstractEvent>(AbstractEvent.class).newInstance();
	}

	public static class SimpleEvent { }
	public static abstract class AbstractEvent { }
}
