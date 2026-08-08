package org.springframework.integration.disruptor.config.workflow.translator;

import java.lang.annotation.Annotation;

import org.springframework.integration.Message;
import org.springframework.integration.disruptor.config.workflow.reflection.AbstractMethodInvoker;
import org.springframework.integration.disruptor.config.workflow.reflection.MethodSpecification;
import org.springframework.util.ReflectionUtils;

/**
 * A MessageEventTranslator adapter that invokes a method on a target object
 * annotated with @EventTranslator to translate messages into ring buffer events.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessageEventTranslatorFactory
 * @see org.springframework.integration.disruptor.config.annotation.EventTranslator
 */
public class MethodInvokingMessageEventTranslator<T> extends AbstractMethodInvoker<T> implements MessageEventTranslator<T> {

	/**
     * Constructs an adapter that delegates translation to a method on the target object.
     *
     * @param target       the object containing the translator method
     * @param expectedType the expected event type
     */
	public MethodInvokingMessageEventTranslator(final Object target, final Class<T> expectedType) {
		super(target, expectedType);
	}

	@Override
	protected MethodSpecification getSpecification(final Class<T> expectedType) {
		final MethodSpecification specification = new MethodSpecification();
		specification.setReturnType(void.class);
		specification.setArgumentTypes(Message.class, this.expectedType);
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
		return "MessageEventTranslator";
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return org.springframework.integration.disruptor.config.annotation.EventTranslator.class;
	}

	/**
     * Translates a message into an event by invoking the resolved method on the target.
     *
     * @param message the source message
     * @param event   the target event
     */
	public void translateTo(final Message<?> message, final T event) {
		ReflectionUtils.invokeMethod(this.method, this.target, message, event);
	}

}
