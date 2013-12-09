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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;
import jodd.util.ReflectUtil;

import org.apache.commons.lang3.StringUtils;

import com.threewks.thundr.action.Action;
import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.introspection.ParameterDescription;

public class MethodAction implements Action {
	private Class<?> class1;
	private Method method;

	private List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

	public MethodAction(Class<?> class1, String methodName) {
		this.class1 = class1;
		this.method = ReflectUtil.findMethod(class1, methodName);
		if (this.method == null) {
			throw new ActionException("Unable to create %s - the method %s.%s does not exist", getClass().getSimpleName(), class1.getName(), methodName);
		}
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
		return class1.getSimpleName() + "." + method.getName();
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> type() {
		return (Class<T>) class1;
	}

	public Method method() {
		return method;
	}

	static final String classNameForAction(String actionName) {
		return StringUtils.substringBeforeLast(actionName, ".");
	}

	static final String methodNameForAction(String actionName) {
		return StringUtils.substringAfterLast(actionName, ".");
	}
}
