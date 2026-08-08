package org.springframework.integration.disruptor.config.workflow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.integration.disruptor.config.HandlerGroup;

/**
 * Unit tests for {@link TraversalContext}.
 */
public class TraversalContextTest {

	@Test
	public void shouldMarkNodesAsVisited() {
		final HandlerGroup group = createGroup("solo", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(group));

		final TraversalContext ctx = new TraversalContext(graph) {};
		assertTrue(ctx.wasNotVisited(0));
		ctx.visit(0);
		assertFalse(ctx.wasNotVisited(0));
	}

	@Test
	public void shouldHandleMultipleNodes() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));

		final TraversalContext ctx = new TraversalContext(graph) {};
		assertTrue(ctx.wasNotVisited(0));
		assertTrue(ctx.wasNotVisited(1));
		assertTrue(ctx.wasNotVisited(2));

		ctx.visit(0);
		assertFalse(ctx.wasNotVisited(0));
		assertTrue(ctx.wasNotVisited(1));
		assertTrue(ctx.wasNotVisited(2));
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
