package org.springframework.integration.disruptor.config.workflow.eventhandler;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.integration.disruptor.config.annotation.EventHandler;

/**
 * Unit tests for {@link MethodInvokingEventHandler}.
 */
public class MethodInvokingEventHandlerTest {

	@Test
	public void shouldInvokeAnnotatedMethod() throws Exception {
		final HandlerTarget target = new HandlerTarget();
		final MethodInvokingEventHandler<SimpleEvent> handler =
				new MethodInvokingEventHandler<SimpleEvent>(target, SimpleEvent.class);
		final SimpleEvent event = new SimpleEvent();
		handler.onEvent(event, 0, false);
		assertSame(event, target.lastEvent);
	}

	@Test
	public void shouldInvokeCompatibleMethod() throws Exception {
		final SimpleHandlerTarget target = new SimpleHandlerTarget();
		final MethodInvokingEventHandler<SimpleEvent> handler =
				new MethodInvokingEventHandler<SimpleEvent>(target, SimpleEvent.class);
		final SimpleEvent event = new SimpleEvent();
		handler.onEvent(event, 42, true);
		assertSame(event, target.lastEvent);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowWhenNoSuitableMethod() {
		new MethodInvokingEventHandler<SimpleEvent>(new Object(), SimpleEvent.class);
	}

	public static class SimpleEvent { }

	public static class HandlerTarget {
		public SimpleEvent lastEvent;

		@EventHandler
		public void handle(SimpleEvent event) {
			this.lastEvent = event;
		}
	}

	public static class SimpleHandlerTarget {
		public SimpleEvent lastEvent;

		public void handle(SimpleEvent event) {
			this.lastEvent = event;
		}
	}
}
