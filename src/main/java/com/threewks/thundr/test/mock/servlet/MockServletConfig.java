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
package com.threewks.thundr.test.mock.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

@SuppressWarnings("rawtypes")
public class MockServletConfig implements ServletConfig {
	private ServletContext servletContext;
	private Map<String, String> initParameters;

	public MockServletConfig() {
		this(new MockServletContext());
	}

	public MockServletConfig(ServletContext servletContext) {
		this(servletContext, new HashMap<String, String>());
	}

	public MockServletConfig(Map<String, String> initParameters) {
		this(new MockServletContext(), initParameters);
	}

	public MockServletConfig(ServletContext servletContext, Map<String, String> initParameters) {
		this.servletContext = servletContext;
		this.initParameters = initParameters;
	}

	@Override
	public String getServletName() {
		return "servlet";
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public String getInitParameter(String name) {
		return initParameters.get(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(initParameters.keySet());
	}
}
