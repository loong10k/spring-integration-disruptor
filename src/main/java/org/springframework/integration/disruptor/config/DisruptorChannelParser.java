package org.springframework.integration.disruptor.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractChannelParser;
import org.springframework.integration.disruptor.DisruptorChannel;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * XML bean definition parser for the disruptor:channel element.
 * Creates a DisruptorChannel bean definition backed by a named disruptor.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see DisruptorChannel
 * @see DisruptorNamespaceElements
 */
public class DisruptorChannelParser extends AbstractChannelParser {

	@Override
	protected BeanDefinitionBuilder buildBeanDefinition(final Element element, final ParserContext parserContext) {

		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DisruptorChannel.class);

		final String disruptor = element.getAttribute(DisruptorNamespaceElements.CHANNEL_ATTRIBUTE_DISRUPTOR);
		if (StringUtils.hasText(disruptor)) {
			builder.addConstructorArgReference(disruptor);
		} else {
			parserContext.getReaderContext().error("The 'disruptor' attribute is required.", element);
		}

		return builder;

	}

}
