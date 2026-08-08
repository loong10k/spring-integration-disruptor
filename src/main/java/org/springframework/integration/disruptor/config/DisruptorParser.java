package org.springframework.integration.disruptor.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * XML bean definition parser for the disruptor:disruptor element.
 * Creates a Disruptor bean definition with the configured event factory,
 * executor, claim strategy, and wait strategy.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractRingBufferParser
 * @see DisruptorNamespaceElements
 */
public class DisruptorParser extends AbstractRingBufferParser {

	@Override
	protected AbstractBeanDefinition parseInternal(final Element element, final ParserContext parserContext) {
		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Disruptor.class);
		this.parseEventFactory(element, builder);
		this.parseExecutor(element, builder);
		final ClaimStrategy claimStrategy = this.parseClaimStrategy(element, parserContext);
		builder.addConstructorArgValue(claimStrategy);
		final WaitStrategy waitStrategy = this.parseWaitStrategy(element, parserContext);
		builder.addConstructorArgValue(waitStrategy);
		return builder.getBeanDefinition();
	}

}
