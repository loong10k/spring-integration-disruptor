package org.springframework.integration.disruptor.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.disruptor.MessagingEventFactory;
import org.w3c.dom.Element;

/**
 * XML bean definition parser for the disruptor:messaging-event-factory element.
 * Creates a MessagingEventFactory bean definition.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessagingEventFactory
 */
public class MessagingEventFactoryParser extends AbstractBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(final Element element, final ParserContext parserContext) {
		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MessagingEventFactory.class);
		return builder.getBeanDefinition();
	}

}
