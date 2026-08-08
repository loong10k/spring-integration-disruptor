package org.springframework.integration.disruptor.config;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.lmax.disruptor.EventProcessor;

/**
 * Data holder representing a named group of event handlers in a disruptor workflow.
 * Each group has a name, a list of dependencies on other groups, handler bean names,
 * handler bean definitions, and the resulting event processors.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see HandlerGroupDefinition
 */
public final class HandlerGroup {

	private String name;
	private List<String> dependencies;
	private List<String> handlerBeanNames;
	private List<BeanDefinitionHolder> handlerBeanDefinitions;
	private List<EventProcessor> eventProcessors;

	/**
     * Returns the name of this handler group.
     *
     * @return the group name
     */
	public String getName() {
		return this.name;
	}

	/**
     * Sets the name of this handler group.
     *
     * @param name the group name
     */
	public void setName(final String name) {
		this.name = name;
	}

	/**
     * Returns the list of dependency group names.
     *
     * @return the dependency names
     */
	public List<String> getDependencies() {
		return this.dependencies;
	}

	/**
     * Sets the list of dependency group names.
     *
     * @param dependencies the dependency names
     */
	public void setDependencies(final List<String> dependencies) {
		this.dependencies = dependencies;
	}

	/**
     * Returns the list of handler bean names in this group.
     *
     * @return the handler bean names
     */
	public List<String> getHandlerBeanNames() {
		return this.handlerBeanNames;
	}

	/**
     * Sets the list of handler bean names.
     *
     * @param handlerBeanNames the handler bean names
     */
	public void setHandlerBeanNames(final List<String> handlerBeanNames) {
		this.handlerBeanNames = handlerBeanNames;
	}

	/**
     * Returns the list of handler bean definitions.
     *
     * @return the handler bean definitions
     */
	public List<BeanDefinitionHolder> getHandlerBeanDefinitions() {
		return this.handlerBeanDefinitions;
	}

	/**
     * Sets the list of handler bean definitions.
     *
     * @param handlerBeanDefinitions the handler bean definitions
     */
	public void setHandlerBeanDefinitions(final List<BeanDefinitionHolder> handlerBeanDefinitions) {
		this.handlerBeanDefinitions = handlerBeanDefinitions;
	}

	/**
     * Returns the list of event processors created for this group.
     *
     * @return the event processors
     */
	public List<EventProcessor> getEventProcessors() {
		return this.eventProcessors;
	}

	/**
     * Sets the list of event processors for this group.
     *
     * @param eventProcessors the event processors
     */
	public void setEventProcessors(final List<EventProcessor> eventProcessors) {
		this.eventProcessors = eventProcessors;
	}

	/**
     * Checks whether this group has exactly one dependency with the given name.
     *
     * @param name the dependency name to check
     * @return {@code true} if this group depends solely on the given name
     */
	public boolean hasSingleDependency(final String name) {
		return (this.dependencies.size() == 1) && name.equals(this.dependencies.get(0));
	}

	@Override
	/**
     * Returns a string representation of this handler group.
     *
     * @return a descriptive string
     */
	public String toString() {
		return "HandlerGroup [name=" + this.name + ", dependencies=" + this.dependencies + ", handlerBeanNames=" + this.handlerBeanNames
				+ ", handlerBeanDefinitions=" + this.handlerBeanDefinitions + ", eventProcessors=" + this.eventProcessors + "]";
	}

}