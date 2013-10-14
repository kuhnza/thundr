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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.ParameterBinderSet;
import com.threewks.thundr.introspection.ParameterDescription;

public class SessionAttributeBinder implements ActionMethodBinder {

	public SessionAttributeBinder() {
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		HttpSession session = req.getSession();
		if (session != null) {
			ParameterBinderSet parameterBinderSet = new ParameterBinderSet();
			Map<String, String[]> requestAttributes = createStringSessionAttributes(session);
			parameterBinderSet.bind(bindings, requestAttributes, null);

			for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
				ParameterDescription key = binding.getKey();
				String name = key.name();
				Object value = session.getAttribute(name);
				if (binding.getValue() == null && value != null && key.isA(value.getClass())) {
					bindings.put(key, value);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, String[]> createStringSessionAttributes(HttpSession session) {
		Map<String, String[]> results = new HashMap<String, String[]>();
		Enumeration<String> attributeNames = session.getAttributeNames();
		if (attributeNames != null) {
			for (String name : Expressive.iterable(attributeNames)) {
				Object value = session.getAttribute(name);
				if (value instanceof String) {
					results.put(name, new String[] { (String) value });
				}
			}
		}
		return results;
	}
}
