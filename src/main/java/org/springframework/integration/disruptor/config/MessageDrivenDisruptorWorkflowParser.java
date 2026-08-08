package org.springframework.integration.disruptor.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.disruptor.config.workflow.MessageDrivenDisruptorWorkflowFactoryBean;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * XML bean definition parser for the disruptor:message-driven-workflow element.
 * Creates a MessageDrivenDisruptorWorkflowFactoryBean bean definition with
 * publisher channel configuration.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractDisruptorWorkflowParser
 * @see config.workflow.MessageDrivenDisruptorWorkflowFactoryBean
 */
public final class MessageDrivenDisruptorWorkflowParser extends AbstractDisruptorWorkflowParser {

	@Override
	protected void doParseInternal(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder) {
		this.parsePublisherChannelNames(element, parserContext, builder);
	}

	private void parsePublisherChannelNames(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder builder) {
		final Element parent = DomUtils.getChildElementByTagName(element, "publisher-channels");
		if (parent != null) {
			final Set<String> publisherChannelNames = this.parsePublisherChannelNames(parent, parserContext);
			builder.addPropertyValue("publisherChannelNames", publisherChannelNames);
		}
	}

	private Set<String> parsePublisherChannelNames(final Element parent, final ParserContext parserContext) {
		final Set<String> publisherChannelNames = new HashSet<String>();
		final List<Element> children = DomUtils.getChildElementsByTagName(parent, "publisher-channel");
		for (final Element child : children) {
			final String publisherChannelRef = child.getAttribute("ref");
			if (StringUtils.hasText(publisherChannelRef)) {
				publisherChannelNames.add(publisherChannelRef);
			} else {
				parserContext.getReaderContext().error("'ref' attribute is mandatory for 'publisher-channel'", child);
			}
		}
		return publisherChannelNames;
	}

	@Override
	protected Class<?> getFactoryClass() {
		return MessageDrivenDisruptorWorkflowFactoryBean.class;
	}

}
