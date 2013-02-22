/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.bind;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jodd.util.ReflectUtil;

import org.junit.Test;

import com.threewks.thundr.action.method.ActionInterceptor;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.introspection.ParameterDescription;

public class ParameterDescriptionTest {
	@Test
	public void shouldReadComplexGenericTypeCorrectly() throws ClassNotFoundException {
		List<ParameterDescription> parameterDescriptions = new MethodAction(TestBindTo.class, ReflectUtil.findMethod(TestBindTo.class, "methodMap"), noInterceptors()).parameters();
		ParameterDescription first = parameterDescriptions.get(0);
		assertThat(first.isGeneric(), is(true));
		assertThat(first.isA(Map.class), is(true));
		assertThat(first.getGenericType(0).equals(String.class), is(true));
		assertThat(first.getGenericType(1), instanceOf(ParameterizedType.class));
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
