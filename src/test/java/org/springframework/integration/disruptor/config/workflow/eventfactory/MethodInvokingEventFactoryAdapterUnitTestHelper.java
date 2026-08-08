package org.springframework.integration.disruptor.config.workflow.eventfactory;

import org.springframework.integration.disruptor.config.annotation.EventFactory;

/**
 * Test helper classes for {@link MethodInvokingEventFactoryAdapterUnitTest}.
 */
public class MethodInvokingEventFactoryAdapterUnitTestHelper {

	public static class SimpleEvent { }

	public static class FactoryTarget {
		public boolean created = false;

		@EventFactory
		public SimpleEvent createEvent() {
			created = true;
			return new SimpleEvent();
		}
	}

	public static class SimpleFactoryTarget {
		public SimpleEvent createEvent() {
			return new SimpleEvent();
		}
	}
}
