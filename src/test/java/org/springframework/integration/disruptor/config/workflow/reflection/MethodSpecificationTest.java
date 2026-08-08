package org.springframework.integration.disruptor.config.workflow.reflection;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.integration.disruptor.config.annotation.EventFactory;

/**
 * Unit tests for {@link MethodSpecification}.
 */
public class MethodSpecificationTest {

	@Test
	public void shouldSetAndGetReturnType() {
		final MethodSpecification spec = new MethodSpecification();
		assertFalse(spec.hasReturnType());
		spec.setReturnType(String.class);
		assertTrue(spec.hasReturnType());
		assertEquals(String.class, spec.getReturnType());
	}

	@Test
	public void shouldSetAndGetAnnotationType() {
		final MethodSpecification spec = new MethodSpecification();
		assertFalse(spec.hasAnnotationType());
		spec.setAnnotationType(EventFactory.class);
		assertTrue(spec.hasAnnotationType());
		assertEquals(EventFactory.class, spec.getAnnotationType());
	}

	@Test
	public void shouldSetAndGetArgumentTypes() {
		final MethodSpecification spec = new MethodSpecification();
		assertFalse(spec.hasArgumentTypes());
		spec.setArgumentTypes(String.class, int.class);
		assertTrue(spec.hasArgumentTypes());
		final Class<?>[] args = spec.getArgumentTypes();
		assertEquals(2, args.length);
		assertEquals(String.class, args[0]);
		assertEquals(int.class, args[1]);
	}

	@Test
	public void shouldSupportEmptyArgumentTypes() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setArgumentTypes();
		assertTrue(spec.hasArgumentTypes());
		assertEquals(0, spec.getArgumentTypes().length);
	}

	@Test
	public void shouldDefaultToNullValues() {
		final MethodSpecification spec = new MethodSpecification();
		assertNull(spec.getReturnType());
		assertNull(spec.getAnnotationType());
		assertNull(spec.getArgumentTypes());
	}
}
