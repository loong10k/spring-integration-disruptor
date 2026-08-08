package org.springframework.integration.disruptor.config.workflow;

/**
 * Base context class for graph traversals. Maintains a visited-node marker array
 * shared by cycle detection and topological sorting algorithms.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see CycleDetectorImpl
 * @see DependencyTopologyBuilderImpl
 */
abstract class TraversalContext {

	private final boolean marked[];

	TraversalContext(final Graph graph) {
		this.marked = new boolean[graph.getSize()];
	}

	protected void visit(final int key) {
		this.marked[key] = true;
	}

	protected boolean wasNotVisited(final int key) {
		return !this.marked[key];
	}

}