package org.springframework.integration.disruptor.config.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.disruptor.config.HandlerGroup;
import org.springframework.integration.disruptor.config.workflow.eventhandler.MethodInvokingEventHandler;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.lmax.disruptor.EventHandler;

/**
 * Factory that creates EventHandler instances for a given handler group.
 * Supports native LMAX event handlers and method-invoking wrappers.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see config.workflow.eventhandler.MethodInvokingEventHandler
 * @see HandlerGroup
 */
final class EventHandlerFactory<T> implements BeanFactoryAware, InitializingBean {

	private final Log log = LogFactory.getLog(this.getClass());

	private BeanFactory beanFactory;

	public void setBeanFactory(final BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
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

	private Map<String, List<EventHandler<T>>> resolvedHandlerMap;

	/**
     * Sets the pre-resolved handler map from XML parsing.
     *
     * @param resolvedHandlerMap the resolved handler map
     */
	public void setResolvedHandlerMap(final Map<String, List<EventHandler<T>>> resolvedHandlerMap) {
		this.resolvedHandlerMap = resolvedHandlerMap;
	}

	/**
     * Creates event handlers for the given handler group by resolving bean names
     * and including pre-resolved handlers.
     *
     * @param handlerGroup the handler group
     * @return the list of event handlers
     */
	public List<EventHandler<T>> createEventHandlers(final HandlerGroup handlerGroup) {
		final List<EventHandler<T>> eventHandlers = new ArrayList<EventHandler<T>>();
		for (final String handlerBeanName : handlerGroup.getHandlerBeanNames()) {
			eventHandlers.add(this.createEventHandler(handlerBeanName));
		}
		final List<EventHandler<T>> resolvedHandlers = this.resolvedHandlerMap.get(handlerGroup.getName());
		for (final EventHandler<T> resolvedHandler : resolvedHandlers) {
			eventHandlers.add(resolvedHandler);
		}
		return eventHandlers;
	}

	/**
     * Creates an event handler for the given bean name. Returns a native handler
     * if compatible, otherwise wraps it in a MethodInvokingEventHandler.
     *
     * @param handlerBeanName the bean name of the handler
     * @return the event handler
     */
	public EventHandler<T> createEventHandler(final String handlerBeanName) {
		Assert.isTrue(StringUtils.hasText(handlerBeanName), "Handler Bean name can not be null or empty.");
		final Object handlerObject = this.beanFactory.getBean(handlerBeanName);
		if (this.isNativeHandler(handlerObject)) {
			this.log.info("'" + handlerBeanName + "' is a native EventHandler");
			@SuppressWarnings("unchecked")
			final EventHandler<T> eventHandler = (EventHandler<T>) handlerObject;
			return eventHandler;
		} else {
			this.log.info("'" + handlerBeanName + "' is a not native EventHandler, wrapping with MethodInvokingEventHandler.");
			return new MethodInvokingEventHandler<T>(handlerObject, this.eventType);
		}
	}

	private boolean isNativeHandler(final Object handlerObject) {
		return (handlerObject instanceof EventHandler)
				&& (ReflectionUtils.findMethod(handlerObject.getClass(), "onEvent", this.eventType, long.class, boolean.class) != null);
	}

	/**
     * Validates that beanFactory and eventType are set.
     *
     * @throws Exception if validation fails
     */
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(this.beanFactory != null, "BeanFactory can not be null.");
		Assert.isTrue(this.eventType != null, "Event type can not be null.");
	}

}