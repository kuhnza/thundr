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
package com.threewks.thundr.action.method.bind.path;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.typeconverter.TypeConverterManager;

import org.joda.time.DateTime;

import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.BasicTypesParameterBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class PathVariableBinder implements ActionMethodBinder {
	{
		// we rely on the static init in this class to register some type convertors
		// this is horrible, i'm not sure how better to handle the reliance on static registration.
		new BasicTypesParameterBinder();
	}
	
	public static final List<Class<?>> PathVariableTypes = Arrays.<Class<?>>
			asList( String.class,
					short.class,
					Short.class,
					int.class,
					Integer.class,
					long.class,
					Long.class,
					float.class,
					Float.class,
					double.class,
					Double.class,
					BigDecimal.class,
					BigInteger.class,
					UUID.class,
					Date.class,
					DateTime.class);

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (ParameterDescription parameterDescription : bindings.keySet()) {
			if (bindings.get(parameterDescription) == null) {
				if (canBindFromPathVariable(parameterDescription)) {
					Object value = bind(parameterDescription, pathVariables);
					bindings.put(parameterDescription, value);
				}
			}
		}
	}

	private boolean canBindFromPathVariable(ParameterDescription parameterDescription) {
		return PathVariableTypes.contains(parameterDescription.type());
	}

	private Object bind(ParameterDescription parameterDescription, Map<String, String> pathVariables) {
		String value = pathVariables.get(parameterDescription.name());
		return TypeConverterManager.lookup(parameterDescription.classType()).convert(value);
	}
}
