package org.springframework.integration.disruptor.config.workflow;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.disruptor.config.workflow.eventfactory.FallbackEventFactoryAdapter;
import org.springframework.integration.disruptor.config.workflow.eventfactory.MethodInvokingEventFactoryAdapter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.lmax.disruptor.EventFactory;

/**
 * Factory that creates an EventFactory for a given event type.
 * Supports native LMAX event factories, method-invoking adapters,
 * and a fallback instantiation strategy.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see config.workflow.eventfactory.FallbackEventFactoryAdapter
 * @see config.workflow.eventfactory.MethodInvokingEventFactoryAdapter
 */
final class EventFactoryFactory<T> implements BeanFactoryAware {

	private final Log log = LogFactory.getLog(this.getClass());

	private BeanFactory beanFactory;

	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	private String name;

	/**
     * Sets the bean name of the event factory to look up.
     *
     * @param name the bean name
     */
	public void setName(final String name) {
		this.name = name;
	}

	private Class<T> eventType;

	/**
     * Sets the event type class.
     *
     * @param eventType the event type
     */
	public void setEventType(final Class<T> eventType) {
		this.eventType = eventType;
	}

	/**
     * Creates an EventFactory. If a name is set, looks up the bean and wraps it
     * if necessary; otherwise returns a fallback factory.
     *
     * @return the event factory
     */
	public EventFactory<T> createEventFactory() {
		if (StringUtils.hasText(this.name)) {
			final Object object = this.beanFactory.getBean(this.name);
			if (this.isNativeEventFactory(object)) {
				this.log.info("Configuring 'workflow' with native EventFactory named '" + this.name + "'.");
				@SuppressWarnings("unchecked")
				final EventFactory<T> eventFactory = (EventFactory<T>) object;
				return eventFactory;
			} else {
				this.log.info("Configuring 'workflow' with MethodInvokingEventFactory named '" + this.name + "'.");
				return new MethodInvokingEventFactoryAdapter<T>(object, this.eventType);
			}
		}
		this.log.info("Configuring 'workflow' with FallbackEventFactory.");
		return new FallbackEventFactoryAdapter<T>(this.eventType);
	}

	/**
     * Checks whether the given object is a native LMAX EventFactory with a
     * compatible return type.
     *
     * @param eventFactory the object to check
     * @return {@code true} if it is a compatible native event factory
     */
	boolean isNativeEventFactory(final Object eventFactory) {
		Assert.isTrue(this.eventType != null, "Event type can not be null");
		if (eventFactory instanceof EventFactory) {
			final Method method = ReflectionUtils.findMethod(eventFactory.getClass(), "newInstance");
			final Class<?> returnType = method.getReturnType();
			return this.eventType.isAssignableFrom(returnType);
		}
		return false;
	}

}