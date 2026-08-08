package org.springframework.integration.disruptor.config;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.lmax.disruptor.EventProcessor;

/**
 * Unit tests for {@link HandlerGroup}.
 */
public class HandlerGroupTest {

	@Test
	public void shouldSetAndGetName() {
		final HandlerGroup group = new HandlerGroup();
		group.setName("myGroup");
		assertEquals("myGroup", group.getName());
	}

	@Test
	public void shouldSetAndGetDependencies() {
		final HandlerGroup group = new HandlerGroup();
		final List<String> deps = Arrays.asList("dep1", "dep2");
		group.setDependencies(deps);
		assertEquals(deps, group.getDependencies());
	}

	@Test
	public void shouldSetAndGetHandlerBeanNames() {
		final HandlerGroup group = new HandlerGroup();
		final List<String> names = Arrays.asList("handler1", "handler2");
		group.setHandlerBeanNames(names);
		assertEquals(names, group.getHandlerBeanNames());
	}

	@Test
	public void shouldSetAndGetHandlerBeanDefinitions() {
		final HandlerGroup group = new HandlerGroup();
		group.setHandlerBeanDefinitions(Collections.emptyList());
		assertNotNull(group.getHandlerBeanDefinitions());
		assertTrue(group.getHandlerBeanDefinitions().isEmpty());
	}

	@Test
	public void shouldSetAndGetEventProcessors() {
		final HandlerGroup group = new HandlerGroup();
		group.setEventProcessors(Collections.emptyList());
		assertNotNull(group.getEventProcessors());
		assertTrue(group.getEventProcessors().isEmpty());
	}

	@Test
	public void shouldReturnTrueForSingleMatchingDependency() {
		final HandlerGroup group = new HandlerGroup();
		group.setDependencies(Arrays.asList("ring-buffer"));
		assertTrue(group.hasSingleDependency("ring-buffer"));
	}

	@Test
	public void shouldReturnFalseForMultipleDependencies() {
		final HandlerGroup group = new HandlerGroup();
		group.setDependencies(Arrays.asList("ring-buffer", "other"));
		assertFalse(group.hasSingleDependency("ring-buffer"));
	}

	@Test
	public void shouldReturnFalseForNonMatchingDependency() {
		final HandlerGroup group = new HandlerGroup();
		group.setDependencies(Arrays.asList("other"));
		assertFalse(group.hasSingleDependency("ring-buffer"));
	}

	@Test
	public void shouldReturnFalseForEmptyDependencies() {
		final HandlerGroup group = new HandlerGroup();
		group.setDependencies(Collections.emptyList());
		assertFalse(group.hasSingleDependency("ring-buffer"));
	}

	@Test
	public void shouldProduceReadableToString() {
		final HandlerGroup group = new HandlerGroup();
		group.setName("test");
		group.setDependencies(Arrays.asList("dep1"));
		group.setHandlerBeanNames(Arrays.asList("handler1"));
		final String str = group.toString();
		assertTrue(str.contains("test"));
		assertTrue(str.contains("dep1"));
		assertTrue(str.contains("handler1"));
	}
}
