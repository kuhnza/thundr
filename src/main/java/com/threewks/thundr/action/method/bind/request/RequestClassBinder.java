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
package com.threewks.thundr.action.method.bind.request;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestClassBinder implements ActionMethodBinder {
	public static final List<Class<?>> BoundTypes = Expressive.<Class<?>> list(HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
			if (binding.getValue() == null) {
				ParameterDescription parameterDescription = binding.getKey();

				Object value = null;
				if (parameterDescription.isA(HttpServletRequest.class)) {
					value = req;
				}
				if (parameterDescription.isA(HttpServletResponse.class)) {
					value = resp;
				}
				if (parameterDescription.isA(HttpSession.class)) {
					value = req.getSession();
				}
				bindings.put(parameterDescription, value);
			}
		}
	}
}
