package com.atomicleopard.webFramework.bind2;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import jodd.introspector.ClassDescriptor;

import org.junit.Test;

import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class ParameterDescriptionTest {
	private ClassDescriptor classDescriptor = new ClassDescriptor(TestBindTo.class, true);

	@Test
	public void shouldReadComplexGenericTypeCorrectly() {
		Method method = classDescriptor.getAllMethods("methodMap")[0];
		List<ParameterDescription> parameterDescriptions = new MethodParameterBinder().getParameterDescriptions(method);
		ParameterDescription first = parameterDescriptions.get(0);
		assertThat(first.isGeneric(), is(true));
		assertThat(first.isA(Map.class), is(true));
		assertThat(first.getGenericType(0).equals(String.class), is(true));
		assertThat(first.getGenericType(1).equals(List.class), is(true));
	}

	@Test
	public void shouldReadSimpleType() {
		Method method = classDescriptor.getAllMethods("methodSingleString")[0];
		List<ParameterDescription> parameterDescriptions = new MethodParameterBinder().getParameterDescriptions(method);
		ParameterDescription first = parameterDescriptions.get(0);
		assertThat(first.isGeneric(), is(false));
		assertThat(first.isA(String.class), is(true));
	}
}
