package org.springframework.integration.disruptor;

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.context.SmartLifecycle;
import org.springframework.integration.Message;
import org.springframework.integration.context.IntegrationObjectSupport;
import org.springframework.integration.disruptor.config.workflow.translator.MessageEventTranslator;
import org.springframework.util.Assert;

import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;

/**
 * Abstract base class for Disruptor-based workflow implementations that integrate
 * with Spring Integration's messaging infrastructure. Manages the lifecycle of
 * a LMAX {@link RingBuffer} and its associated {@link EventProcessor}s, providing
 * start/stop semantics via {@link SmartLifecycle}.
 *
 * <p>Subclasses must define how messages are sent or handled by implementing
 * the appropriate Spring Integration channel or handler interfaces.</p>
 *
 * @param <T> the event type stored in the ring buffer
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @see DisruptorWorkflow
 * @see MessageDrivenDisruptorWorkflow
 * @see com.lmax.disruptor.RingBuffer
 * @since 3.0.0
 */
public abstract class AbstractDisruptorWorkflow<T> extends IntegrationObjectSupport implements SmartLifecycle {

	private volatile boolean running = false;
	private volatile boolean autoStartup = true;
	private volatile int phase = 0;

	private final RingBuffer<T> ringBuffer;
	private final Executor executor;
	private final List<EventProcessor> eventProcessors;
	private final MessageEventTranslator<T> messageEventTranslator;
	private final EventPublisher<T> eventPublisher;

	/**
	 * Constructs a new workflow with the given ring buffer, executor, event processors,
	 * and message-to-event translator.
	 *
	 * @param ringBuffer            the LMAX ring buffer that stores events; must not be {@code null}
	 * @param executor              the executor used to run event processors; must not be {@code null}
	 * @param eventProcessors       the list of event processors to manage; must not be {@code null}
	 * @param messageEventTranslator the translator that converts Spring messages to ring buffer events; must not be {@code null}
	 * @throws IllegalArgumentException if any argument is {@code null}
	 */
	AbstractDisruptorWorkflow(final RingBuffer<T> ringBuffer, final Executor executor, final List<EventProcessor> eventProcessors,
			final MessageEventTranslator<T> messageEventTranslator) {
		Assert.isTrue(ringBuffer != null, "RingBuffer can not be null");
		Assert.isTrue(executor != null, "Executor can not be null");
		Assert.isTrue(eventProcessors != null, "EventProcessors can not be null");
		Assert.isTrue(messageEventTranslator != null, "MessageEventTranslator can not be null");
		this.ringBuffer = ringBuffer;
		this.executor = executor;
		this.eventProcessors = eventProcessors;
		this.messageEventTranslator = messageEventTranslator;
		this.eventPublisher = new EventPublisher<T>(this.ringBuffer);
	}

	/**
	 * Starts this workflow by invoking the {@link #doStart()} template method,
	 * then submitting all event processors to the executor.
	 */
	public final void start() {
		this.doStart();
		this.startEventProcessors();
		this.running = true;
	}

	/**
     * Template method called during startup. Subclasses may override to perform
     * additional startup logic.
     */
	protected void doStart() {
	}

	/**
     * Stops this workflow by halting all event processors and invoking doStop.
     */
	public final void stop() {
		this.running = false;
		this.stopEventProcessors();
		this.doStop();
	}

	/**
     * Template method called during shutdown. Subclasses may override to perform
     * additional shutdown logic.
     */
	protected void doStop() {
	}

	/**
     * Checks whether this workflow is currently running.
     *
     * @return {@code true} if running
     */
	public final boolean isRunning() {
		return this.running;
	}

	/**
     * Returns the lifecycle phase.
     *
     * @return the phase value
     */
	public final int getPhase() {
		return this.phase;
	}

	/**
     * Checks whether this workflow should start automatically.
     *
     * @return {@code true} if auto-startup is enabled
     */
	public final boolean isAutoStartup() {
		return this.autoStartup;
	}

	/**
     * Stops this workflow and runs the callback.
     *
     * @param callback the callback to run after stopping
     */
	public final void stop(final Runnable callback) {
		this.stop();
		callback.run();
	}

	/**
     * Sets the lifecycle phase.
     *
     * @param phase the phase value
     */
	public void setPhase(final int phase) {
		this.phase = phase;
	}

	/**
     * Sets whether this workflow should start automatically.
     *
     * @param autoStartup {@code true} to enable auto-startup
     */
	public void setAutoStartup(final boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	private void startEventProcessors() {
		for (final EventProcessor eventProcessor : this.eventProcessors) {
			this.executor.execute(eventProcessor);
		}
	}

	private void stopEventProcessors() {
		for (final EventProcessor eventProcessor : this.eventProcessors) {
			eventProcessor.halt();
		}
	}

	/**
     * Publishes a message to the ring buffer via the event publisher.
     *
     * @param message the message to publish
     * @return {@code true} if published successfully, {@code false} on error
     */
	protected final boolean publish(final Message<?> message) {
		try {
			this.eventPublisher.publishEvent(new EventTranslator<T>() {

				public void translateTo(final T event, final long sequence) {
					AbstractDisruptorWorkflow.this.messageEventTranslator.translateTo(message, event);
				}

			});
			return true;
		} catch (final Exception e) {
			this.logger.warn("Can't publish to RingBuffer", e);
			return false;
		}
	}

}
