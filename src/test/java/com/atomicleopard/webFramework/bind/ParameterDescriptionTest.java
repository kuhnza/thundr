package com.atomicleopard.webFramework.bind;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jodd.util.ReflectUtil;

import org.junit.Test;

import com.atomicleopard.webFramework.action.method.ActionInterceptor;
import com.atomicleopard.webFramework.action.method.MethodAction;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class ParameterDescriptionTest {
	@Test
	public void shouldReadComplexGenericTypeCorrectly() throws ClassNotFoundException {
		List<ParameterDescription> parameterDescriptions = new MethodAction(TestBindTo.class, ReflectUtil.findMethod(TestBindTo.class, "methodMap"), noInterceptors()).parameters();
		ParameterDescription first = parameterDescriptions.get(0);
		assertThat(first.isGeneric(), is(true));
		assertThat(first.isA(Map.class), is(true));
		assertThat(first.getGenericType(0).equals(String.class), is(true));
		assertThat(first.getGenericType(1), is(ParameterizedType.class));
		assertThat(((ParameterizedType) first.getGenericType(1)).getRawType().equals(List.class), is(true));
		assertThat(((ParameterizedType) first.getGenericType(1)).getActualTypeArguments()[0].equals(String.class), is(true));
	}

	@Test
	public void shouldReadSimpleType() throws ClassNotFoundException {
		List<ParameterDescription> parameterDescriptions = new MethodAction(TestBindTo.class, ReflectUtil.findMethod(TestBindTo.class, "methodSingleString"), noInterceptors()).parameters();
		ParameterDescription first = parameterDescriptions.get(0);
		assertThat(first.isGeneric(), is(false));
		assertThat(first.isA(String.class), is(true));
	}

	private Map<Annotation, ActionInterceptor<Annotation>> noInterceptors() {
		return Collections.<Annotation, ActionInterceptor<Annotation>> emptyMap();
	}

}
