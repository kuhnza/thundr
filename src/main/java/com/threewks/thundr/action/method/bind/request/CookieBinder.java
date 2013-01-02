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

import static com.atomicleopard.expressive.Expressive.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class CookieBinder implements ActionMethodBinder {
	private static final ETransformer<Collection<Cookie>, Map<String, List<Cookie>>> toLookup = Expressive.Transformers.<Cookie, String> toBeanLookup("name", Cookie.class);
	private HttpBinder delegate;

	public CookieBinder(HttpBinder delegate) {
		this.delegate = delegate;
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (req.getCookies() != null) {
			Map<String, List<Cookie>> lookup = toLookup.from(list(req.getCookies()));
			for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
				ParameterDescription key = binding.getKey();
				String name = key.name();
				List<Cookie> namedCookies = lookup.get(name);
				Cookie cookie = isNotEmpty(namedCookies) ? namedCookies.get(0) : null;
				if (key.isA(Cookie.class)) {
					bindings.put(key, cookie);
				} else if (binding.getValue() == null && cookie != null) {
					Map<String, String[]> parameterMap = map(name, array(cookie.getValue()));
					delegate.bind(bindings, req, resp, parameterMap);
				}
			}
		}
	}
}
