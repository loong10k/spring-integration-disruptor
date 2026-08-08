package org.springframework.integration.disruptor.config.workflow;

/**
 * Strategy interface for detecting cycles in a directed Graph.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CycleDetectorImpl
 * @see Graph
 */
interface CycleDetector {

	boolean hasCycle(Graph graph);

}