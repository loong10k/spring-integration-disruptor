package org.springframework.integration.disruptor.config.annotation;

public @/**
 * Method-level annotation that designates a handler method for processing
 * ring buffer events. Used by MethodInvokingEventHandler to disambiguate
 * when multiple candidate methods exist.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see config.workflow.eventhandler.MethodInvokingEventHandler
 */
interface EventHandler {

}
