package org.springframework.integration.disruptor.config.workflow;

import java.util.List;

/**
 * Strategy interface for building a topological ordering of a DependencyGraph.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see DependencyTopologyBuilderImpl
 * @see DependencyGraph
 */
interface DependencyTopologyBuilder {

	List<String> buildTopology(DependencyGraph graph);

}
