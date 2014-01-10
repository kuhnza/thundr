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
package com.threewks.thundr.action.method.bind.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.Expressive;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.action.method.bind.request.CookieBinder;
import com.threewks.thundr.action.method.bind.request.RequestClassBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.json.GsonSupport;

public class GsonBinder implements ActionMethodBinder {
	public static final List<Class<?>> NonBindableTypes = Expressive.list(PathVariableBinder.PathVariableTypes).addItems(RequestClassBinder.BoundTypes).addItems(CookieBinder.BoundTypes);

	private GsonBuilder gsonBuilder;

	public GsonBinder() {
		this(GsonSupport.createBasicGsonBuilder());
	}

	public GsonBinder(GsonBuilder gsonBuilder) {
		this.gsonBuilder = gsonBuilder;
	}

	/**
	 * Exposes the underlying builder, allowing the modification of how Json is bound.
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return gsonBuilder;
	}

	public boolean canBind(String contentType) {
		return ContentType.ApplicationJson.value().equalsIgnoreCase(contentType);
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (!bindings.isEmpty()) {
			String sanitisedContentType = ContentType.cleanContentType(req.getContentType());
			if (canBind(sanitisedContentType)) {
				ParameterDescription jsonParameterDescription = findParameterDescriptionForJsonParameter(bindings);
				if (jsonParameterDescription != null) {
					try {
						Gson gson = gsonBuilder.create();
						Object converted = gson.fromJson(req.getReader(), jsonParameterDescription.type());
						bindings.put(jsonParameterDescription, converted);
					} catch (IOException e) {
						throw new BindException(e, "Failed to bind %s %s using JSON - can only bind the first object parameter", jsonParameterDescription.type(), jsonParameterDescription.name());
					}
				}
			}
		}
	}

	private ParameterDescription findParameterDescriptionForJsonParameter(Map<ParameterDescription, Object> bindings) {
		for (Map.Entry<ParameterDescription, Object> bindingEntry : bindings.entrySet()) {
			ParameterDescription parameterDescription = bindingEntry.getKey();
			if (bindingEntry.getValue() == null && !NonBindableTypes.contains(parameterDescription.type())) {
				return parameterDescription;
			}
		}
		return null;
	}
}
