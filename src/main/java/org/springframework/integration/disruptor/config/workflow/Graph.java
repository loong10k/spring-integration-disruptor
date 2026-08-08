package org.springframework.integration.disruptor.config.workflow;

import java.util.List;

/**
 * Minimal directed graph interface providing node count and adjacency queries.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see DependencyGraph
 * @see CycleDetector
 */
interface Graph {

	int getSize();

	List<Integer> adjacentKeys(Integer key);

}
