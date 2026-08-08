package org.springframework.integration.disruptor.config.workflow.eventhandler;

import java.lang.annotation.Annotation;

import org.springframework.integration.disruptor.config.workflow.reflection.AbstractMethodInvoker;
import org.springframework.integration.disruptor.config.workflow.reflection.MethodSpecification;
import org.springframework.util.ReflectionUtils;

import com.lmax.disruptor.EventHandler;

/**
 * An EventHandler adapter that invokes a method on a target object annotated
 * with @EventHandler for each ring buffer event.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see EventHandlerFactory
 * @see org.springframework.integration.disruptor.config.annotation.EventHandler
 */
public final class MethodInvokingEventHandler<T> extends AbstractMethodInvoker<T> implements EventHandler<T> {

	/**
     * Constructs an adapter that delegates event handling to a method on the target object.
     *
     * @param target       the object containing the handler method
     * @param expectedType the expected event type
     */
	public MethodInvokingEventHandler(final Object target, final Class<T> expectedType) {
		super(target, expectedType);
	}

	/**
     * Handles a ring buffer event by invoking the resolved method on the target.
     *
     * @param event       the event from the ring buffer
     * @param sequence    the sequence number of the event
     * @param endOfBatch  whether this is the last event in the current batch
     * @throws Exception if the method invocation fails
     */
	public void onEvent(final T event, final long sequence, final boolean endOfBatch) throws Exception {
		ReflectionUtils.invokeMethod(this.method, this.target, event);
	}

	@Override
	protected MethodSpecification getSpecification(final Class<T> expectedType) {
		final MethodSpecification specification = new MethodSpecification();
		specification.setArgumentTypes(expectedType);
		specification.setReturnType(void.class);
		return specification;
	}

	@Override
	protected MethodSpecification getNarrowingSpecification() {
		final MethodSpecification specification = new MethodSpecification();
		specification.setAnnotationType(this.getAnnotationType());
		return specification;
	}

	@Override
	protected String getDescription() {
		return "EventHandler";
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return org.springframework.integration.disruptor.config.annotation.EventHandler.class;
	}

}
