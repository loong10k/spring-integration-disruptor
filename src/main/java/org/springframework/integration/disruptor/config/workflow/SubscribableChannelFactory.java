package org.springframework.integration.disruptor.config.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.core.SubscribableChannel;

/**
 * Factory that resolves named SubscribableChannel beans from the Spring BeanFactory.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MessageDrivenDisruptorWorkflowFactoryBean
 */
final class SubscribableChannelFactory implements BeanFactoryAware {

	private BeanFactory beanFactory;

	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	private Set<String> publisherChannelNames;

	/**
     * Sets the publisher channel bean names to resolve.
     *
     * @param publisherChannelNames the channel bean names
     */
	public void setPublisherChannelNames(final Set<String> publisherChannelNames) {
		this.publisherChannelNames = publisherChannelNames;
	}

	/**
     * Resolves and returns the subscribable channels from the BeanFactory.
     *
     * @return the list of subscribable channels
     */
	public List<SubscribableChannel> createSubscribableChannels() {
		final List<SubscribableChannel> subscribableChannels = new ArrayList<SubscribableChannel>();
		for (final String publisherChannelName : this.publisherChannelNames) {
			final SubscribableChannel subscribableChannel = this.beanFactory.getBean(publisherChannelName, SubscribableChannel.class);
			subscribableChannels.add(subscribableChannel);
		}
		return subscribableChannels;
	}

}