package org.springframework.integration.disruptor.config.workflow;

import java.util.List;

import org.springframework.integration.disruptor.config.workflow.DependencyGraphImpl.DependencySetter;

/**
 * Interface for a dependency graph that maps symbolic names to integer keys
 * and tracks directed dependencies between them. Extends Graph for traversal support.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see Graph
 * @see DependencyGraphImpl
 * @see HandlerGroupDefinition
 */
interface DependencyGraph extends Graph {

	List<String> getDependencies(String depender);

	List<String> getSymbolicNames();

	List<String> toSymbolicNames(Iterable<Integer> keys);

	DependencySetter addDependency(String depender);

	List<String> getOrphanDependencies();

	DependencyGraph inverse();

}