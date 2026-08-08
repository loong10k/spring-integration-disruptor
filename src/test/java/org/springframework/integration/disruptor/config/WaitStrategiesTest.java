package org.springframework.integration.disruptor.config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;

/**
 * Unit tests for {@link WaitStrategies}.
 */
public class WaitStrategiesTest {

	@Test
	public void shouldFindBlockingByName() {
		final WaitStrategies result = WaitStrategies.find("blocking");
		assertEquals(WaitStrategies.BLOCKING, result);
	}

	@Test
	public void shouldFindBusySpinByName() {
		final WaitStrategies result = WaitStrategies.find("busy-spin");
		assertEquals(WaitStrategies.BUSY_SPIN, result);
	}

	@Test
	public void shouldFindYieldingByName() {
		final WaitStrategies result = WaitStrategies.find("yielding");
		assertEquals(WaitStrategies.YIELDING, result);
	}

	@Test
	public void shouldFindSleepingByName() {
		final WaitStrategies result = WaitStrategies.find("sleeping");
		assertEquals(WaitStrategies.SLEEPING, result);
	}

	@Test
	public void shouldReturnNullForUnknownName() {
		final WaitStrategies result = WaitStrategies.find("unknown");
		assertNull(result);
	}

	@Test
	public void shouldCreateBlockingInstance() {
		final WaitStrategy strategy = WaitStrategies.forName("blocking");
		assertNotNull(strategy);
		assertTrue(strategy instanceof BlockingWaitStrategy);
	}

	@Test
	public void shouldCreateBusySpinInstance() {
		final WaitStrategy strategy = WaitStrategies.forName("busy-spin");
		assertNotNull(strategy);
		assertTrue(strategy instanceof BusySpinWaitStrategy);
	}

	@Test
	public void shouldCreateYieldingInstance() {
		final WaitStrategy strategy = WaitStrategies.forName("yielding");
		assertNotNull(strategy);
		assertTrue(strategy instanceof YieldingWaitStrategy);
	}

	@Test
	public void shouldCreateSleepingInstance() {
		final WaitStrategy strategy = WaitStrategies.forName("sleeping");
		assertNotNull(strategy);
		assertTrue(strategy instanceof SleepingWaitStrategy);
	}

	@Test
	public void shouldReturnNullForUnknownStrategyName() {
		final WaitStrategy strategy = WaitStrategies.forName("unknown");
		assertNull(strategy);
	}

	@Test
	public void shouldCreateInstanceViaEnumMethod() {
		final WaitStrategy strategy = WaitStrategies.BLOCKING.newInstance();
		assertNotNull(strategy);
		assertTrue(strategy instanceof BlockingWaitStrategy);
	}
}
