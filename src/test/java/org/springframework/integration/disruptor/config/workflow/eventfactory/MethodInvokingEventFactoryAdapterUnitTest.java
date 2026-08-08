package org.springframework.integration.disruptor.config.workflow.eventfactory;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.integration.disruptor.config.annotation.EventFactory;

/**
 * Unit tests for {@link MethodInvokingEventFactoryAdapter}.
 */
public class MethodInvokingEventFactoryAdapterUnitTest {

	@Test
	public void shouldCreateViaAnnotatedMethod() {
		final MethodInvokingEventFactoryAdapterUnitTestHelper.FactoryTarget target =
				new MethodInvokingEventFactoryAdapterUnitTestHelper.FactoryTarget();
		final MethodInvokingEventFactoryAdapter<MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent> factory =
				new MethodInvokingEventFactoryAdapter<MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent>(
						target, MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent.class);
		final MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent event = factory.newInstance();
		assertNotNull(event);
		assertTrue(target.created);
	}

	@Test
	public void shouldCreateViaCompatibleMethod() {
		final MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleFactoryTarget target =
				new MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleFactoryTarget();
		final MethodInvokingEventFactoryAdapter<MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent> factory =
				new MethodInvokingEventFactoryAdapter<MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent>(
						target, MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent.class);
		final MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent event = factory.newInstance();
		assertNotNull(event);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenNoSuitableMethod() {
		new MethodInvokingEventFactoryAdapter<MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent>(
				new Object(), MethodInvokingEventFactoryAdapterUnitTestHelper.SimpleEvent.class);
	}
}
