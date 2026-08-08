package org.springframework.integration.disruptor.config.workflow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.integration.disruptor.config.HandlerGroup;

/**
 * Unit tests for {@link DependencyGraphImpl}.
 */
public class DependencyGraphUnitTest {

	@Test
	public void shouldBuildGraphFromHandlerGroups() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");

		final Iterable<HandlerGroup> groups = Arrays.asList(groupA, groupB);
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(groups);

		assertNotNull(graph);
		assertEquals(3, graph.getSize()); // groupA, groupB, ring-buffer
	}

	@Test
	public void shouldReturnDependenciesForGroup() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");

		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));
		final List<String> deps = graph.getDependencies("groupB");
		assertEquals(1, deps.size());
		assertEquals("groupA", deps.get(0));
	}

	@Test
	public void shouldReturnEmptyDependenciesForUnknownGroup() {
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.<HandlerGroup>emptyList());
		final List<String> deps = graph.getDependencies("unknown");
		assertTrue(deps.isEmpty());
	}

	@Test
	public void shouldReturnOrphanDependencies() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(groupA));
		final List<String> orphans = graph.getOrphanDependencies();
		assertTrue(orphans.contains("ring-buffer"));
	}

	@Test
	public void shouldReturnSymbolicNames() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(groupA));
		final List<String> names = graph.getSymbolicNames();
		assertTrue(names.contains("groupA"));
		assertTrue(names.contains("ring-buffer"));
	}

	@Test
	public void shouldInverseGraph() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");

		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));
		final DependencyGraph inverse = graph.inverse();

		assertNotNull(inverse);
		assertEquals(graph.getSize(), inverse.getSize());
	}

	@Test
	public void shouldConvertKeysToSymbolicNames() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(groupA));
		final List<String> names = graph.toSymbolicNames(Arrays.asList(0, 1));
		assertEquals(2, names.size());
	}

	@Test
	public void shouldReturnAdjacentKeys() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(groupA));
		assertNotNull(graph.adjacentKeys(0));
	}

	@Test
	public void shouldReturnZeroSizeForEmptyGraph() {
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.<HandlerGroup>emptyList());
		assertEquals(0, graph.getSize());
	}

	@Test
	public void shouldHandleMultipleDependencies() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "ring-buffer");
		final HandlerGroup groupC = createGroup("groupC", "groupA,groupB");
		// Manually set multiple dependencies
		groupC.setDependencies(Arrays.asList("groupA", "groupB"));

		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB, groupC));
		final List<String> deps = graph.getDependencies("groupC");
		assertEquals(2, deps.size());
	}

	@Test
	public void shouldBuildGraphWithSingleGroupDependingOnRingBuffer() {
		final HandlerGroup group = createGroup("solo", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(group));
		assertEquals(2, graph.getSize());
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
