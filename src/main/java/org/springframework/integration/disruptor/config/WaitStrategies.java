package org.springframework.integration.disruptor.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

/**
 * Enumeration of supported LMAX Disruptor wait strategies. Maps human-readable
 * names to their corresponding WaitStrategy implementations.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see ClaimStrategies
 * @see com.lmax.disruptor.WaitStrategy
 */
public enum WaitStrategies {

	BLOCKING("blocking") {

		@Override
		public WaitStrategy newInstance() {
			return new BlockingWaitStrategy();
		}

	},

	BUSY_SPIN("busy-spin") {

		@Override
		public WaitStrategy newInstance() {
			return new BusySpinWaitStrategy();
		}

	},

	YIELDING("yielding") {

		@Override
		public WaitStrategy newInstance() {
			return new YieldingWaitStrategy();
		}

	},

	SLEEPING("sleeping") {

		@Override
		public WaitStrategy newInstance() {
			return new SleepingWaitStrategy();
		}

	};

	private String name;

	private WaitStrategies(final String name) {
		this.name = name;
	}

	/**
     * Finds a wait strategy enum constant by its human-readable name.
     *
     * @param name the strategy name (e.g. "blocking", "busy-spin")
     * @return the matching constant, or {@code null} if not found
     */
	public static WaitStrategies find(final String name) {
		if (BLOCKING.name.equals(name)) {
			return BLOCKING;
		} else if (BUSY_SPIN.name.equals(name)) {
			return BUSY_SPIN;
		} else if (YIELDING.name.equals(name)) {
			return YIELDING;
		} else if (SLEEPING.name.equals(name)) {
			return SLEEPING;
		} else {
			return null;
		}
	}

	/**
     * Creates a new WaitStrategy instance for the given name.
     *
     * @param name the strategy name
     * @return a new wait strategy instance, or {@code null} if the name is unknown
     */
	public static WaitStrategy forName(final String name) {
		final WaitStrategies found = find(name);
		if (found != null) {
			return found.newInstance();
		} else {
			return null;
		}
	}

	/**
     * Creates a new instance of this wait strategy.
     *
     * @return a new wait strategy instance
     */
	public abstract WaitStrategy newInstance();

}
