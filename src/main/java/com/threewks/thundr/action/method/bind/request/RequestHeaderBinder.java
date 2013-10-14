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

import static com.atomicleopard.expressive.Expressive.list;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.ParameterBinderSet;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestHeaderBinder implements ActionMethodBinder {
	public RequestHeaderBinder() {

	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		ParameterBinderSet parameterBinderSet = new ParameterBinderSet();
		Map<String, String[]> parameterMap = createNormalisedHeaderMap(req);
		parameterBinderSet.bind(bindings, parameterMap, null);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String[]> createNormalisedHeaderMap(HttpServletRequest req) {
		Map<String, String[]> results = new HashMap<String, String[]>();
		Enumeration<String> headerNames = req.getHeaderNames();
		if (headerNames != null) {
			for (String name : Expressive.iterable(headerNames)) {
				String[] values = headerValues(req.getHeaders(name));
				results.put(normaliseHeaderName(name), values);
			}
		}
		return results;
	}

	String normaliseHeaderName(String header) {
		String capitalised = WordUtils.capitalizeFully(header, '-');
		String withoutDashes = capitalised.replaceAll(StringPool.DASH, StringPool.EMPTY);
		return StringUtils.uncapitalize(withoutDashes);
	}

	private String[] headerValues(Enumeration<String> headers) {
		return list(Expressive.iterable(headers)).toArray(new String[0]);
	}
}
