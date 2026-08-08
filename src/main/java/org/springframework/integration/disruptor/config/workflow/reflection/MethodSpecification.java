package org.springframework.integration.disruptor.config.workflow.reflection;

import java.lang.annotation.Annotation;

/**
 * Specification object used by MethodFinderUtils to describe the desired
 * return type, annotation type, and argument types when searching for methods
 * via reflection.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see MethodFinderUtils
 * @see AbstractMethodInvoker
 */
public class MethodSpecification {

	private Class<?> returnType;
	private Class<? extends Annotation> annotationType;
	private Class<?>[] argumentTypes;

	/**
     * Returns the expected return type.
     *
     * @return the return type, or {@code null} if not set
     */
	public Class<?> getReturnType() {
		return this.returnType;
	}

	/**
     * Sets the expected return type.
     *
     * @param returnType the return type
     */
	public void setReturnType(final Class<?> returnType) {
		this.returnType = returnType;
	}

	/**
     * Returns the expected annotation type.
     *
     * @return the annotation type, or {@code null} if not set
     */
	public Class<? extends Annotation> getAnnotationType() {
		return this.annotationType;
	}

	/**
     * Sets the expected annotation type.
     *
     * @param annotationType the annotation type
     */
	public void setAnnotationType(final Class<? extends Annotation> annotationType) {
		this.annotationType = annotationType;
	}

	/**
     * Returns the expected argument types.
     *
     * @return the argument types, or {@code null} if not set
     */
	public Class<?>[] getArgumentTypes() {
		return this.argumentTypes;
	}

	/**
     * Sets the expected argument types.
     *
     * @param argumentTypes the argument types
     */
	public void setArgumentTypes(final Class<?>... argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	/**
     * Checks whether a return type has been specified.
     *
     * @return {@code true} if a return type is set
     */
	public boolean hasReturnType() {
		return this.returnType != null;
	}

	/**
     * Checks whether an annotation type has been specified.
     *
     * @return {@code true} if an annotation type is set
     */
	public boolean hasAnnotationType() {
		return this.annotationType != null;
	}

	/**
     * Checks whether argument types have been specified.
     *
     * @return {@code true} if argument types are set
     */
	public boolean hasArgumentTypes() {
		return this.argumentTypes != null;
	}

}
