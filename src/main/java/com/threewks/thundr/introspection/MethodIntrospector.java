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
package com.threewks.thundr.introspection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;
import jodd.paramo.ParamoException;

import com.atomicleopard.expressive.Cast;

public class MethodIntrospector {
	public List<ParameterDescription> getParameterDescriptions(AccessibleObject methodOrCtor) {
		Type[] genericParameterTypes = getGenericParameterTypes(methodOrCtor);
		String[] names = getParameterNames(methodOrCtor);
		return mergeToParameters(names, genericParameterTypes);
	}

	private List<ParameterDescription> mergeToParameters(String[] names, Type[] types) {
		List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
		for (int i = 0; i < types.length; i++) {
			parameters.add(new ParameterDescription(names[i], types[i]));
		}
		return parameters;
	}

	private Type[] getGenericParameterTypes(AccessibleObject methodOrCtor) {
		Method method = Cast.as(methodOrCtor, Method.class);
		Constructor<?> ctor = Cast.as(methodOrCtor, Constructor.class);
		return method != null ? method.getGenericParameterTypes() : ctor.getGenericParameterTypes();
	}

	/**
	 * Paramo fails if there are no parameters (for example, a default ctor)
	 * 
	 * @param methodOrCtor
	 * @return
	 */
	private String[] getParameterNames(AccessibleObject methodOrCtor) {
		try {
			MethodParameter[] parameterNames = Paramo.resolveParameters(methodOrCtor);
			String[] names = new String[parameterNames.length];
			for (int i = 0; i < parameterNames.length; i++) {
				names[i] = parameterNames[i].getName();
			}
			return names;
		} catch (ParamoException e) {
			// parameter names are only available when the classes were debug compiled - so if you use a library or JDK class, Paramo cannot help
			Method method = Cast.as(methodOrCtor, Method.class);
			Constructor<?> constructor = Cast.as(methodOrCtor, Constructor.class);
			int parameterCount = method == null ? constructor.getParameterTypes().length : method.getParameterTypes().length;
			String[] names = new String[parameterCount];
			for (int i = 0; i < names.length; i++) {
				names[i] = "Unknown";
			}
			return names;
		}
	}
}
