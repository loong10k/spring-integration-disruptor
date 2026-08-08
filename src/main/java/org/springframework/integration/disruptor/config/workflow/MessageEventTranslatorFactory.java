package org.springframework.integration.disruptor.config.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.Message;
import org.springframework.integration.disruptor.MessagingEvent;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;
import org.springframework.integration.disruptor.config.workflow.translator.MessagingEventTranslator;
import org.springframework.integration.disruptor.config.workflow.translator.MethodInvokingMessageEventTranslator;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * Factory that creates a MessageEventTranslator for translating Spring Integration
 * messages into ring buffer events. Supports native translators, method-invoking
 * adapters, and a default MessagingEventTranslator when the event type is MessagingEvent.
 * 
 * @param <T> the event type
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see config.workflow.translator.MessageEventTranslator
 * @see config.workflow.translator.MessagingEventTranslator
 */
final class MessageEventTranslatorFactory<T> implements BeanFactoryAware {

	private final Log log = LogFactory.getLog(this.getClass());

	private BeanFactory beanFactory;

	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
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

	private String translatorName;

	/**
     * Sets the bean name of the translator to look up.
     *
     * @param translatorName the translator bean name
     */
	public void setTranslatorName(final String translatorName) {
		this.translatorName = translatorName;
	}

	/**
     * Creates a MessageEventTranslator. If a name is set, looks up the bean;
     * for MessagingEvent types, returns a default translator; otherwise throws.
     *
     * @return the message event translator
     * @throws BeanCreationException if no translator can be created
     */
	public MessageEventTranslator<T> createTranslator() {
		if (StringUtils.hasText(this.translatorName)) {
			final Object translator = this.beanFactory.getBean(this.translatorName);
			if (this.isNativeTranslator(translator)) {
				this.log.info("'" + this.translatorName + "' is a native MessageEventTranslator.");
				@SuppressWarnings("unchecked")
				final MessageEventTranslator<T> messageToEventTranslator = (MessageEventTranslator<T>) translator;
				return messageToEventTranslator;
			} else {
				this.log.info("'" + this.translatorName + "' is not a native MessageEventTranslator, configuring MethodInvokingMessageEventTranslator.");
				return new MethodInvokingMessageEventTranslator<T>(translator, this.eventType);
			}
		} else {
			if (this.isMessagingEventType()) {
				this.log.info("'MessagingEvent' event type found, configuring default MessageEventTranslator");
				@SuppressWarnings("unchecked")
				final MessageEventTranslator<T> messagingEventTranslator = (MessageEventTranslator<T>) new MessagingEventTranslator();
				return messagingEventTranslator;
			} else {
				throw new BeanCreationException("Can't create 'workflow' without MessageEventTranslator (the one exception "
						+ "to this rule is when event type is MessagingEvent or empty)");
			}
		}
	}

	private boolean isNativeTranslator(final Object translator) {
		return (translator instanceof MessageEventTranslator)
				&& (ReflectionUtils.findMethod(translator.getClass(), "translateTo", Message.class, this.eventType) != null);
	}

	private boolean isMessagingEventType() {
		return MessagingEvent.class.isAssignableFrom(this.eventType);
	}

}