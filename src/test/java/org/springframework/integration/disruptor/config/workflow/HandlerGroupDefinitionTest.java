package org.springframework.integration.disruptor.config.workflow;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.integration.disruptor.config.HandlerGroup;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;

/**
 * Unit tests for {@link HandlerGroupDefinition}.
 */
public class HandlerGroupDefinitionTest {

	@Test
	public void shouldReturnHandlerGroupByName() {
		final HandlerGroup group = new HandlerGroup();
		group.setName("groupA");
		group.setDependencies(Collections.singletonList("ring-buffer"));
		group.setHandlerBeanNames(Collections.singletonList("handler1"));
		group.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", group);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		assertSame(group, def.getHandlerGroup("groupA"));
		assertNull(def.getHandlerGroup("nonExistent"));
	}

	@Test
	public void shouldReturnAllEventProcessors() {
		final HandlerGroup groupA = new HandlerGroup();
		groupA.setName("groupA");
		groupA.setDependencies(Collections.singletonList("ring-buffer"));
		groupA.setHandlerBeanNames(Collections.singletonList("h1"));
		groupA.setEventProcessors(Collections.<EventProcessor>emptyList());

		final HandlerGroup groupB = new HandlerGroup();
		groupB.setName("groupB");
		groupB.setDependencies(Collections.singletonList("groupA"));
		groupB.setHandlerBeanNames(Collections.singletonList("h2"));
		groupB.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", groupA);
		groups.put("groupB", groupB);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		final List<EventProcessor> all = def.getAllEventProcessors();
		assertNotNull(all);
		assertTrue(all.isEmpty());
	}

	@Test
	public void shouldCreateDependencyGraphWithoutCycles() {
		final HandlerGroup groupA = new HandlerGroup();
		groupA.setName("groupA");
		groupA.setDependencies(Collections.singletonList("ring-buffer"));
		groupA.setHandlerBeanNames(Collections.singletonList("h1"));
		groupA.setEventProcessors(Collections.<EventProcessor>emptyList());

		final HandlerGroup groupB = new HandlerGroup();
		groupB.setName("groupB");
		groupB.setDependencies(Collections.singletonList("groupA"));
		groupB.setHandlerBeanNames(Collections.singletonList("h2"));
		groupB.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", groupA);
		groups.put("groupB", groupB);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		final DependencyGraph graph = def.createDependencyGraph();
		assertNotNull(graph);
	}

	@Test(expected = BeanCreationException.class)
	public void shouldDetectCircularDependency() {
		final HandlerGroup groupA = new HandlerGroup();
		groupA.setName("groupA");
		groupA.setDependencies(Collections.singletonList("groupB"));
		groupA.setHandlerBeanNames(Collections.singletonList("h1"));
		groupA.setEventProcessors(Collections.<EventProcessor>emptyList());

		final HandlerGroup groupB = new HandlerGroup();
		groupB.setName("groupB");
		groupB.setDependencies(Collections.singletonList("groupA"));
		groupB.setHandlerBeanNames(Collections.singletonList("h2"));
		groupB.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", groupA);
		groups.put("groupB", groupB);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		def.createDependencyGraph();
	}

	@Test
	public void shouldReturnEventProcessorsForGroup() {
		final HandlerGroup group = new HandlerGroup();
		group.setName("groupA");
		group.setDependencies(Collections.singletonList("ring-buffer"));
		group.setHandlerBeanNames(Collections.singletonList("h1"));
		group.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", group);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		def.addEventProcessors("groupA", Collections.<EventProcessor>emptyList());
		assertNotNull(def.getEventProcessors("groupA"));
	}

	@Test
	public void shouldReturnSequencesForGroup() {
		final HandlerGroup group = new HandlerGroup();
		group.setName("groupA");
		group.setDependencies(Collections.singletonList("ring-buffer"));
		group.setHandlerBeanNames(Collections.singletonList("h1"));
		group.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", group);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		def.addEventProcessors("groupA", Collections.<EventProcessor>emptyList());
		final List<Sequence> sequences = def.getSequences("groupA");
		assertNotNull(sequences);
	}

	@Test
	public void shouldReturnAllSequencesForMultipleGroups() {
		final HandlerGroup groupA = new HandlerGroup();
		groupA.setName("groupA");
		groupA.setDependencies(Collections.singletonList("ring-buffer"));
		groupA.setHandlerBeanNames(Collections.singletonList("h1"));
		groupA.setEventProcessors(Collections.<EventProcessor>emptyList());

		final Map<String, HandlerGroup> groups = new HashMap<String, HandlerGroup>();
		groups.put("groupA", groupA);

		final HandlerGroupDefinition def = new HandlerGroupDefinition(groups);
		def.addEventProcessors("groupA", Collections.<EventProcessor>emptyList());
		final List<Sequence> sequences = def.getAllSequences(Arrays.asList("groupA"));
		assertNotNull(sequences);
	}
}
