package org.springframework.integration.disruptor.config.workflow.eventfactory;

import org.springframework.beans.BeanUtils;

import com.lmax.disruptor.EventFactory;

/**
 * A fallback EventFactory that creates event instances using BeanUtils.instantiate.
 * Used when no explicit event factory is configured for the workflow.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see EventFactoryFactory
 */
public class FallbackEventFactoryAdapter<T> implements EventFactory<T> {

	private final Class<T> expectedType;

	/**
     * Constructs a fallback factory for the given event type.
     *
     * @param expectedType the event class to instantiate
     */
	public FallbackEventFactoryAdapter(final Class<T> expectedType) {
		this.expectedType = expectedType;
	}

	/**
     * Creates a new event instance using BeanUtils.instantiate.
     *
     * @return a new event instance
     */
	public T newInstance() {
		return BeanUtils.instantiate(this.expectedType);
	}
}
