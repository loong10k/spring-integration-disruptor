package org.springframework.integration.disruptor.config.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.integration.disruptor.config.HandlerGroup;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.Sequence;

/**
 * Aggregates all HandlerGroups for a disruptor workflow and provides methods
 * to manage event processors, sequences, and dependency graph creation.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see HandlerGroup
 * @see config.workflow.DependencyGraph
 */
public final class HandlerGroupDefinition {

	private final Map<String, HandlerGroup> handlerGroups;
	private final Map<String, List<Sequence>> sequencesForGroups;

	/**
     * Constructs a new definition from the given handler group map.
     *
     * @param handlerGroups the map of group names to handler groups
     */
	public HandlerGroupDefinition(final Map<String, HandlerGroup> handlerGroups) {
		this.handlerGroups = handlerGroups;
		this.sequencesForGroups = new HashMap<String, List<Sequence>>(this.handlerGroups.size());
	}

	/**
     * Returns the handler group with the given name.
     *
     * @param handlerGroupName the group name
     * @return the handler group, or {@code null} if not found
     */
	public HandlerGroup getHandlerGroup(final String handlerGroupName) {
		return this.handlerGroups.get(handlerGroupName);
	}

	/**
     * Registers event processors for the given handler group.
     *
     * @param forHandlerGroup the handler group name
     * @param eventProcessors the event processors to register
     */
	public void addEventProcessors(final String forHandlerGroup, final List<EventProcessor> eventProcessors) {
		this.addEventProcessorsToHandlerGroup(forHandlerGroup, eventProcessors);
		this.addSequencesToHandlerGroup(forHandlerGroup, eventProcessors);
	}

	private void addSequencesToHandlerGroup(final String forHandler, final List<EventProcessor> eventProcessors) {
		this.handlerGroups.get(forHandler).setEventProcessors(eventProcessors);
	}

	private void addEventProcessorsToHandlerGroup(final String forHandler, final List<EventProcessor> eventProcessors) {
		this.sequencesForGroups.put(forHandler, getSequences(eventProcessors));
	}

	private static List<Sequence> getSequences(final List<EventProcessor> eventProcessors) {
		final List<Sequence> sequences = new ArrayList<Sequence>(eventProcessors.size());
		for (final EventProcessor eventProcessor : eventProcessors) {
			sequences.add(eventProcessor.getSequence());
		}
		return sequences;
	}

	/**
     * Returns all event processors across all handler groups.
     *
     * @return a combined list of all event processors
     */
	public List<EventProcessor> getAllEventProcessors() {
		final List<EventProcessor> allEventProcessors = new ArrayList<EventProcessor>();
		for (final HandlerGroup handlerGroup : this.handlerGroups.values()) {
			allEventProcessors.addAll(handlerGroup.getEventProcessors());
		}
		return allEventProcessors;
	}

	/**
     * Returns the event processors for the given handler group.
     *
     * @param forHandlerGroup the handler group name
     * @return the event processors for the group
     */
	public List<EventProcessor> getEventProcessors(final String forHandlerGroup) {
		return this.handlerGroups.get(forHandlerGroup).getEventProcessors();
	}

	/**
     * Returns the sequences for the given handler group.
     *
     * @param handlerGroupName the handler group name
     * @return the sequences
     */
	public List<Sequence> getSequences(final String handlerGroupName) {
		return this.sequencesForGroups.get(handlerGroupName);
	}

	/**
     * Returns all sequences for the given handler group names.
     *
     * @param handlerGroupNames the handler group names
     * @return a combined list of all sequences
     */
	public List<Sequence> getAllSequences(final Iterable<String> handlerGroupNames) {
		final List<Sequence> allSequences = new ArrayList<Sequence>();
		for (final String handlerGroupName : handlerGroupNames) {
			allSequences.addAll(this.getSequences(handlerGroupName));
		}
		return allSequences;
	}

	/**
     * Creates a dependency graph from the handler groups and validates that no cycles exist.
     *
     * @return the dependency graph
     * @throws BeanCreationException if a circular dependency is detected
     */
	public DependencyGraph createDependencyGraph() {
		final DependencyGraph dependencyGraph = DependencyGraphImpl.forHandlerGroups(this.handlerGroups.values());
		detectDependencyCycle(dependencyGraph);
		return dependencyGraph;
	}

	private static void detectDependencyCycle(final DependencyGraph dependencyGraph) {
		final CycleDetector cycleDetector = new CycleDetectorImpl();
		if (cycleDetector.hasCycle(dependencyGraph)) {
			throw new BeanCreationException("Circular 'handler-group' dependency detected while creating DisruptorWorkflow");
		}
	}

}
