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
package com.threewks.thundr.action.method.bind.http;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.UUID;

import org.joda.time.DateTime;

import jodd.typeconverter.TypeConverterManager;

import com.threewks.thundr.action.method.bind.http.converters.DateTimeTypeConverter;
import com.threewks.thundr.action.method.bind.http.converters.UUIDTypeConverter;
import com.threewks.thundr.introspection.ParameterDescription;

public class BasicTypesParameterBinder implements ParameterBinder<Object> {

	/**
	 * Enhance Jodds basic type converters for our additional basic types
	 */
	{
		TypeConverterManager.register(DateTime.class, new DateTimeTypeConverter());
		TypeConverterManager.register(UUID.class, new UUIDTypeConverter());
	}

	public Object bind(ParameterBinderSet binder, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] values = pathMap.get(list(parameterDescription.name()));
		return values != null && values.length > 0 ? TypeConverterManager.lookup(parameterDescription.classType()).convert(values[0]) : null;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return TypeConverterManager.lookup(parameterDescription.classType()) != null;
	}
}
