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

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.threewks.thundr.collection.factory.MapFactory;
import com.threewks.thundr.introspection.ParameterDescription;

/**
 * Binds to a collection using the given Factory. Supports two types of list - indexed and unindexed.
 * An indexed list looks like this:
 * list[0]=value
 * list[1]=value
 * 
 * and unindexed list looks like this:
 * list=value,value
 * 
 */
public class MapParameterBinder<T extends Map<Object, Object>> implements ParameterBinder<T> {
	private static final Pattern elementPattern = Pattern.compile("\\[(.+)\\]");
	private MapFactory<T> mapFactory;

	public MapParameterBinder(MapFactory<T> mapFactory) {
		this.mapFactory = mapFactory;
	}

	public T bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		pathMap = pathMap.pathMapFor(parameterDescription.name());
		Set<String> children = pathMap.uniqueChildren();
		if (children.isEmpty()) {
			return null;
		}

		T mapParameter = mapFactory.create();
		for (String string : children) {
			Matcher matcher = elementPattern.matcher(string);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(String.format("Cannot bind %s%s - not a valid map key", parameterDescription.name(), string));
			}
			String name = matcher.group(1);
			Type genericType = parameterDescription.getGenericType(1);
			ParameterDescription parameter = new ParameterDescription(string, genericType);
			Object mapEntry = binders.createFor(parameter, pathMap);
			mapParameter.put(name, mapEntry);
		}
		return mapParameter;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.isA(mapFactory.forType());
	}

	@Override
	public String toString() {
		return this.getClass() + " for " + mapFactory.forType();
	}
}
