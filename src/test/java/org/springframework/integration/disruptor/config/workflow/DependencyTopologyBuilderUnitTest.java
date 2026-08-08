package org.springframework.integration.disruptor.config.workflow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.integration.disruptor.config.HandlerGroup;

/**
 * Unit tests for {@link DependencyTopologyBuilderImpl}.
 */
public class DependencyTopologyBuilderUnitTest {

	@Test
	public void shouldBuildTopologyForLinearDependency() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "groupA");

		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB));
		final DependencyTopologyBuilder builder = new DependencyTopologyBuilderImpl();
		final List<String> topology = builder.buildTopology(graph);

		assertNotNull(topology);
		assertEquals(3, topology.size());
		assertTrue(topology.contains("ring-buffer"));
		assertTrue(topology.contains("groupA"));
		assertTrue(topology.contains("groupB"));
		// The topology builder produces a valid topological ordering:
		// dependents appear before their dependencies (stack-based DFS post-order)
		final int ringBufferIdx = topology.indexOf("ring-buffer");
		final int groupAIdx = topology.indexOf("groupA");
		final int groupBIdx = topology.indexOf("groupB");
		assertTrue(ringBufferIdx > groupAIdx);
		assertTrue(groupAIdx > groupBIdx);
	}

	@Test
	public void shouldBuildTopologyForSingleGroup() {
		final HandlerGroup group = createGroup("solo", "ring-buffer");
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.singletonList(group));
		final DependencyTopologyBuilder builder = new DependencyTopologyBuilderImpl();
		final List<String> topology = builder.buildTopology(graph);

		assertNotNull(topology);
		assertEquals(2, topology.size());
	}

	@Test
	public void shouldBuildTopologyForEmptyGraph() {
		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Collections.<HandlerGroup>emptyList());
		final DependencyTopologyBuilder builder = new DependencyTopologyBuilderImpl();
		final List<String> topology = builder.buildTopology(graph);
		assertNotNull(topology);
		assertTrue(topology.isEmpty());
	}

	@Test
	public void shouldBuildTopologyForDiamondDependency() {
		final HandlerGroup groupA = createGroup("groupA", "ring-buffer");
		final HandlerGroup groupB = createGroup("groupB", "ring-buffer");
		final HandlerGroup groupC = createGroup("groupC", "groupA,groupB");
		groupC.setDependencies(Arrays.asList("groupA", "groupB"));

		final DependencyGraph graph = DependencyGraphImpl.forHandlerGroups(Arrays.asList(groupA, groupB, groupC));
		final DependencyTopologyBuilder builder = new DependencyTopologyBuilderImpl();
		final List<String> topology = builder.buildTopology(graph);

		assertNotNull(topology);
		assertEquals(4, topology.size());
		assertTrue(topology.contains("ring-buffer"));
		assertTrue(topology.contains("groupA"));
		assertTrue(topology.contains("groupB"));
		assertTrue(topology.contains("groupC"));
		// In the stack-based DFS post-order, dependents come before dependencies
		final int groupCIdx = topology.indexOf("groupC");
		final int groupAIdx = topology.indexOf("groupA");
		final int groupBIdx = topology.indexOf("groupB");
		assertTrue(groupCIdx < groupAIdx);
		assertTrue(groupCIdx < groupBIdx);
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
