package org.springframework.integration.disruptor.config;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.MultiThreadedLowContentionClaimStrategy;
import com.lmax.disruptor.SingleThreadedClaimStrategy;

/**
 * Enumeration of supported LMAX Disruptor claim strategies. Maps human-readable
 * names to their corresponding ClaimStrategy implementations.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see WaitStrategies
 * @see com.lmax.disruptor.ClaimStrategy
 */
public enum ClaimStrategies {

	MULTI_THREADED("multi-threaded") {

		@Override
		public ClaimStrategy newInstance(final int bufferSize) {
			return new MultiThreadedClaimStrategy(bufferSize);
		}

	},

	MULTI_THREADED_LOW_CONTENTION("multi-threaded-low-contention") {

		@Override
		public ClaimStrategy newInstance(final int bufferSize) {
			return new MultiThreadedLowContentionClaimStrategy(bufferSize);
		}

	},

	SINGLE_THREADED("single-threaded") {

		@Override
		public ClaimStrategy newInstance(final int bufferSize) {
			return new SingleThreadedClaimStrategy(bufferSize);
		}

	};

	private String name;

	private ClaimStrategies(final String name) {
		this.name = name;
	}

	/**
     * Finds a claim strategy enum constant by its human-readable name.
     *
     * @param name the strategy name (e.g. "multi-threaded", "single-threaded")
     * @return the matching constant, or {@code null} if not found
     */
	public static ClaimStrategies find(final String name) {
		if (MULTI_THREADED.name.equals(name)) {
			return MULTI_THREADED;
		} else if (MULTI_THREADED_LOW_CONTENTION.name.equals(name)) {
			return MULTI_THREADED_LOW_CONTENTION;
		} else if (SINGLE_THREADED.name.equals(name)) {
			return SINGLE_THREADED;
		} else {
			return null;
		}
	}

	/**
     * Creates a new ClaimStrategy instance for the given name and buffer size.
     *
     * @param name       the strategy name
     * @param bufferSize the ring buffer size
     * @return a new claim strategy instance, or {@code null} if the name is unknown
     */
	public static ClaimStrategy forName(final String name, final int bufferSize) {
		final ClaimStrategies found = find(name);
		if (found != null) {
			return found.newInstance(bufferSize);
		} else {
			return null;
		}
	}

	/**
     * Creates a new instance of this claim strategy with the given buffer size.
     *
     * @param bufferSize the ring buffer size
     * @return a new claim strategy instance
     */
	public abstract ClaimStrategy newInstance(int bufferSize);

}
