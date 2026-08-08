package org.springframework.integration.disruptor.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @/**
 * Method-level annotation that designates a translator method for converting
 * Spring Integration messages into ring buffer events. Used by
 * MethodInvokingMessageEventTranslator to disambiguate when multiple
 * candidate methods exist.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see config.workflow.translator.MethodInvokingMessageEventTranslator
 */
interface EventTranslator {

}
