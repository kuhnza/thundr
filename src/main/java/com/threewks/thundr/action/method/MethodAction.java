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
package com.threewks.thundr.action.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import org.apache.commons.lang3.StringUtils;

import com.threewks.thundr.action.Action;
import com.threewks.thundr.introspection.ParameterDescription;

public class MethodAction implements Action {
	private Class<?> class1;
	private Method method;
	private Map<Annotation, ActionInterceptor<Annotation>> interceptors;

	private List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

	public MethodAction(Class<?> class1, Method method, Map<Annotation, ActionInterceptor<Annotation>> interceptors) {
		this.class1 = class1;
		this.method = method;
		this.interceptors = interceptors;
		Type[] genericParameters = method.getGenericParameterTypes();
		MethodParameter[] parameterNames = Paramo.resolveParameters(method);
		for (int i = 0; i < genericParameters.length; i++) {
			String name = parameterNames[i].getName();
			this.parameters.add(new ParameterDescription(name, genericParameters[i]));
		}
	}

	public List<ParameterDescription> parameters() {
		return parameters;
	}

	public Object invoke(Object controller, List<?> args) throws Exception {
		return method.invoke(controller, args.toArray());
	}

	@Override
	public String toString() {
		return method.toString();
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> type() {
		return (Class<T>) class1;
	}

	public Method method() {
		return method;
	}

	public Map<Annotation, ActionInterceptor<Annotation>> interceptors() {
		return interceptors;
	}

	static final String classNameForAction(String actionName) {
		return StringUtils.substringBeforeLast(actionName, ".");
	}

	static final String methodNameForAction(String actionName) {
		return StringUtils.substringAfterLast(actionName, ".");
	}
}
