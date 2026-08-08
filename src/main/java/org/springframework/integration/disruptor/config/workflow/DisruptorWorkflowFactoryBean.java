package org.springframework.integration.disruptor.config.workflow;

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.integration.disruptor.DisruptorWorkflow;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.util.Assert;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.RingBuffer;

/**
 * FactoryBean that creates DisruptorWorkflow instances. Optionally wraps the
 * workflow in a GatewayProxyFactoryBean when a gateway interface is configured.
 * 
 * @param <T> the event type stored in the ring buffer
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractDisruptorWorkflowFactoryBean
 * @see DisruptorWorkflow
 */
public final class DisruptorWorkflowFactoryBean<T> extends AbstractDisruptorWorkflowFactoryBean<T> implements FactoryBean<Object> {

	private volatile Class<?> interfaceClass;
	private volatile GatewayProxyFactoryBean proxyFactory;

	/**
     * Sets the gateway interface class. When set, the factory bean returns a proxy
     * instead of the raw DisruptorWorkflow.
     *
     * @param interfaceClass the interface class; must be an interface if not {@code null}
     * @throws IllegalArgumentException if the class is not an interface
     */
	public void setInterfaceClass(final Class<?> interfaceClass) {
		if (interfaceClass != null) {
			Assert.isTrue(interfaceClass.isInterface(), interfaceClass.getName() + " is not an interface.");
			this.interfaceClass = interfaceClass;
		}
	}

	@Override
	protected DisruptorWorkflow<T> createInstance(final RingBuffer<T> ringBuffer, final Executor executor, final List<EventProcessor> eventProcessors,
			final MessageEventTranslator<T> messageEventTranslator) {
		return new DisruptorWorkflow<T>(ringBuffer, executor, eventProcessors, messageEventTranslator);
	}

	/**
     * Returns the DisruptorWorkflow instance or a gateway proxy.
     *
     * @return the workflow or gateway proxy
     * @throws Exception if creation fails
     */
	public Object getObject() throws Exception {
		if (this.interfaceClass == null) {
			return this.getInstance();
		} else {
			if (this.proxyFactory == null) {
				this.configureGatewayProxyFactory((DisruptorWorkflow<T>) this.getInstance());
			}
			return this.proxyFactory.getObject();
		}
	}

	private void configureGatewayProxyFactory(final DisruptorWorkflow<T> messageChannel) {
		this.proxyFactory = new GatewayProxyFactoryBean(this.interfaceClass);
		this.proxyFactory.setDefaultRequestChannel(messageChannel);
	}

	/**
     * Returns the type of object produced by this factory.
     *
     * @return the interface class if set, otherwise {@link DisruptorWorkflow}
     */
	public Class<?> getObjectType() {
		return this.interfaceClass != null ? this.interfaceClass : DisruptorWorkflow.class;
	}

	/**
     * Returns {@code true} as this factory produces a singleton.
     *
     * @return always {@code true}
     */
	public boolean isSingleton() {
		return true;
	}

}
