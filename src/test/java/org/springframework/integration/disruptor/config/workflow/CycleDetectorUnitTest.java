package org.springframework.integration.disruptor.config.workflow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.springframework.integration.disruptor.config.HandlerGroup;

/**
 * Unit tests for {@link CycleDetectorImpl}.
 */
public class CycleDetectorUnitTest {

	@Test
	public void shouldDetectNoCycleInAcyclicGraph() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));

		final CycleDetector detector = new CycleDetectorImpl();
		assertFalse(detector.hasCycle(graph));
	}

	@Test
	public void shouldDetectCycleInCyclicGraph() {
		final HandlerGroup groupA = createGroup("groupA", "groupB");
		final HandlerGroup groupB = createGroup("groupB", "groupA");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));

		final CycleDetector detector = new CycleDetectorImpl();
		assertTrue(detector.hasCycle(graph));
	}

	@Test
	public void shouldDetectNoCycleInEmptyGraph() {
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.<HandlerGroup>emptyList());
		final CycleDetector detector = new CycleDetectorImpl();
		assertFalse(detector.hasCycle(graph));
	}

	@Test
	public void shouldDetectNoCycleInSingleNodeGraph() {
		final HandlerGroup group = createGroup("solo", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(group));
		final CycleDetector detector = new CycleDetectorImpl();
		assertFalse(detector.hasCycle(graph));
	}

	@Test
	public void shouldDetectSelfCycle() {
		final HandlerGroup group = createGroup("self", "self");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(group));
		final CycleDetector detector = new CycleDetectorImpl();
		assertTrue(detector.hasCycle(graph));
	}

	private HandlerGroup createGroup(final String name, final String... deps) {
		final HandlerGroup group = new HandlerGroup();
		group.setName(name);
		group.setDependencies(Arrays.asList(deps));
		group.setHandlerBeanNames(Collections.singletonList("handler"));
		group.setEventProcessors(Collections.<com.lmax.disruptor.EventProcessor>emptyList());
		return group;
	}
}
