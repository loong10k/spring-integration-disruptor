package org.springframework.integration.disruptor.config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.MultiThreadedLowContentionClaimStrategy;
import com.lmax.disruptor.SingleThreadedClaimStrategy;

/**
 * Unit tests for {@link ClaimStrategies}.
 */
public class ClaimStrategiesTest {

	@Test
	public void shouldFindMultiThreadedByName() {
		final ClaimStrategies result = ClaimStrategies.find("multi-threaded");
		assertEquals(ClaimStrategies.MULTI_THREADED, result);
	}

	@Test
	public void shouldFindMultiThreadedLowContentionByName() {
		final ClaimStrategies result = ClaimStrategies.find("multi-threaded-low-contention");
		assertEquals(ClaimStrategies.MULTI_THREADED_LOW_CONTENTION, result);
	}

	@Test
	public void shouldFindSingleThreadedByName() {
		final ClaimStrategies result = ClaimStrategies.find("single-threaded");
		assertEquals(ClaimStrategies.SINGLE_THREADED, result);
	}

	@Test
	public void shouldReturnNullForUnknownName() {
		final ClaimStrategies result = ClaimStrategies.find("unknown");
		assertNull(result);
	}

	@Test
	public void shouldCreateMultiThreadedInstance() {
		final ClaimStrategy strategy = ClaimStrategies.forName("multi-threaded", 1024);
		assertNotNull(strategy);
		assertTrue(strategy instanceof MultiThreadedClaimStrategy);
	}

	@Test
	public void shouldCreateMultiThreadedLowContentionInstance() {
		final ClaimStrategy strategy = ClaimStrategies.forName("multi-threaded-low-contention", 1024);
		assertNotNull(strategy);
		assertTrue(strategy instanceof MultiThreadedLowContentionClaimStrategy);
	}

	@Test
	public void shouldCreateSingleThreadedInstance() {
		final ClaimStrategy strategy = ClaimStrategies.forName("single-threaded", 1024);
		assertNotNull(strategy);
		assertTrue(strategy instanceof SingleThreadedClaimStrategy);
	}

	@Test
	public void shouldReturnNullForUnknownStrategyName() {
		final ClaimStrategy strategy = ClaimStrategies.forName("unknown", 1024);
		assertNull(strategy);
	}

	@Test
	public void shouldCreateInstanceViaEnumMethod() {
		final ClaimStrategy strategy = ClaimStrategies.SINGLE_THREADED.newInstance(512);
		assertNotNull(strategy);
		assertTrue(strategy instanceof SingleThreadedClaimStrategy);
	}
}
