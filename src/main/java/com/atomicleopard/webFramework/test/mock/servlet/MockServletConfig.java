package com.atomicleopard.webFramework.test.mock.servlet;

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
