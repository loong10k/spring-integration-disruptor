package org.springframework.integration.disruptor.config.workflow.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for {@link MethodFinderUtils}.
 */
public class MethodFinderUnitTest {

	@Test
	public void shouldFindMethodsByReturnType() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setReturnType(String.class);
		final List<Method> methods = MethodFinderUtils.findMethods(SampleClass.class, spec);
		assertFalse(methods.isEmpty());
		boolean found = false;
		for (final Method m : methods) {
			if (m.getName().equals("getString") && m.getReturnType().equals(String.class)) {
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	public void shouldFindMethodsByArgumentTypes() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setArgumentTypes(String.class);
		spec.setReturnType(void.class);
		final List<Method> methods = MethodFinderUtils.findMethods(SampleClass.class, spec);
		assertFalse(methods.isEmpty());
		boolean found = false;
		for (final Method m : methods) {
			if (m.getName().equals("setString")) {
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	public void shouldFindMethodsByAnnotation() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setAnnotationType(org.springframework.integration.disruptor.config.annotation.EventFactory.class);
		final List<Method> methods = MethodFinderUtils.findMethods(AnnotatedClass.class, spec);
		assertEquals(1, methods.size());
		assertEquals("annotatedMethod", methods.get(0).getName());
	}

	@Test
	public void shouldReturnEmptyForNoMatch() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setReturnType(Double.class);
		final List<Method> methods = MethodFinderUtils.findMethods(SampleClass.class, spec);
		assertTrue(methods.isEmpty());
	}

	@Test
	public void shouldFindMethodsOnObject() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setReturnType(String.class);
		final List<Method> methods = MethodFinderUtils.findMethods(new SampleClass(), spec);
		assertFalse(methods.isEmpty());
	}

	@Test
	public void shouldFindMethodsInList() {
		final MethodSpecification spec = new MethodSpecification();
		spec.setReturnType(String.class);
		final List<Method> allMethods = java.util.Arrays.asList(SampleClass.class.getDeclaredMethods());
		final List<Method> methods = MethodFinderUtils.findMethods(allMethods, spec);
		assertFalse(methods.isEmpty());
	}

	@Test
	public void shouldThrowOnDirectInstantiation() throws Exception {
		final Constructor<MethodFinderUtils> ctor = MethodFinderUtils.class.getDeclaredConstructor();
		ctor.setAccessible(true);
		try {
			ctor.newInstance();
			fail("Expected InvocationTargetException wrapping IllegalStateException");
		} catch (final InvocationTargetException e) {
			assertTrue(e.getCause() instanceof IllegalStateException);
		}
	}

	@SuppressWarnings("unused")
	public static class SampleClass {
		public String getString() { return ""; }
		public void setString(String s) { }
		public int getInt() { return 0; }
	}

	@SuppressWarnings("unused")
	public static class AnnotatedClass {
		@org.springframework.integration.disruptor.config.annotation.EventFactory
		public Object annotatedMethod() { return null; }
		public String otherMethod() { return ""; }
	}
}
