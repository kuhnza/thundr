package com.threewks.thundr.test.mock.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import jodd.util.MimeTypes;

@SuppressWarnings("rawtypes")
public class MockServletContext implements ServletContext {
	private Map<String, String> initParameters;
	private Map<String, Object> attributes;
	private MockRequestDispatcher requestDispatcher = new MockRequestDispatcher();

	public MockServletContext() {
		this(new HashMap<String, Object>(), new HashMap<String, String>());
	}

	public MockServletContext(Map<String, Object> attributes) {
		this(attributes, new HashMap<String, String>());
	}

	public MockServletContext(Map<String, Object> attributes, Map<String, String> initParameters) {
		this.initParameters = initParameters;
		this.attributes = attributes;
	}

	@Override
	public String getContextPath() {
		return "/";
	}

	@Override
	public ServletContext getContext(String uripath) {
		return this;
	}

	@Override
	public int getMajorVersion() {
		return 2;
	}

	@Override
	public int getMinorVersion() {
		return 5;
	}

	@Override
	public String getMimeType(String file) {
		int lastIndexOf = file.lastIndexOf(".");
		String find = lastIndexOf > -1 ? file.substring(lastIndexOf + 1) : "";
		return MimeTypes.lookupMimeType(find);
	}

	@Override
	public Set getResourcePaths(String path) {
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return new URL("http", "localhost", path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		if (!path.startsWith("/")) {
			throw new IllegalArgumentException("RequestDispatcher path at ServletContext level must start with '/'");
		}
		requestDispatcher.lastPath(path);
		return requestDispatcher;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return requestDispatcher;
	}

	public MockRequestDispatcher requestDispatcher() {
		return requestDispatcher;
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		return null;
	}

	@Override
	public Enumeration getServlets() {
		return Collections.enumeration(Collections.emptyList());
	}

	@Override
	public Enumeration getServletNames() {
		return Collections.enumeration(Collections.emptyList());
	}

	@Override
	public void log(String msg) {
		System.out.println(msg);
	}

	@Override
	public void log(Exception exception, String msg) {
		System.out.println(msg);
		exception.printStackTrace(System.out);
	}

	@Override
	public void log(String message, Throwable throwable) {
		System.out.println(message);
		throwable.printStackTrace(System.out);
	}

	@Override
	public String getRealPath(String path) {
		return path;
	}

	@Override
	public String getServerInfo() {
		return "";
	}

	@Override
	public String getInitParameter(String name) {
		return initParameters.get(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(initParameters.keySet());
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public String getServletContextName() {
		return "MockServletContext";
	}

}
