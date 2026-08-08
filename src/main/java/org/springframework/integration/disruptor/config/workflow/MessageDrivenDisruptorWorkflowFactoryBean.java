package org.springframework.integration.disruptor.config.workflow;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.disruptor.MessageDrivenDisruptorWorkflow;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;

/**
 * FactoryBean that creates MessageDrivenDisruptorWorkflow instances with
 * publisher channel support.
 * 
 * @param <T> the event type stored in the ring buffer
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractDisruptorWorkflowFactoryBean
 * @see MessageDrivenDisruptorWorkflow
 */
public final class MessageDrivenDisruptorWorkflowFactoryBean<T> extends AbstractDisruptorWorkflowFactoryBean<T> implements
		FactoryBean<MessageDrivenDisruptorWorkflow<T>> {

	private Set<String> publisherChannelNames;

	/**
     * Sets the names of publisher channels to subscribe to.
     *
     * @param publisherChannelNames the channel bean names
     */
	public void setPublisherChannelNames(final Set<String> publisherChannelNames) {
		this.publisherChannelNames = publisherChannelNames;
	}

	/**
     * Returns the MessageDrivenDisruptorWorkflow instance.
     *
     * @return the workflow instance
     * @throws Exception if creation fails
     */
	public MessageDrivenDisruptorWorkflow<T> getObject() throws Exception {
		return (MessageDrivenDisruptorWorkflow<T>) this.getInstance();
	}

	/**
     * Returns {@code true} as this factory produces a singleton.
     *
     * @return always {@code true}
     */
	public boolean isSingleton() {
		return true;
	}

	/**
     * Returns the type of object produced by this factory.
     *
     * @return {@link MessageDrivenDisruptorWorkflow}
     */
	public Class<?> getObjectType() {
		return MessageDrivenDisruptorWorkflow.class;
	}

	@Override
	protected MessageDrivenDisruptorWorkflow<T> createInstance(final RingBuffer<T> ringBuffer, final Executor executor,
			final List<EventProcessor> eventProcessors, final MessageEventTranslator<T> messageEventTranslator) {

		final SubscribableChannelFactory subscribableChannelFactory = new SubscribableChannelFactory();
		subscribableChannelFactory.setBeanFactory(this.beanFactory);
		subscribableChannelFactory.setPublisherChannelNames(this.publisherChannelNames);
		initialize(subscribableChannelFactory);

		final List<SubscribableChannel> subscribableChannels = subscribableChannelFactory.createSubscribableChannels();

		return new MessageDrivenDisruptorWorkflow<T>(ringBuffer, executor, eventProcessors, messageEventTranslator, subscribableChannels);

	}

}
