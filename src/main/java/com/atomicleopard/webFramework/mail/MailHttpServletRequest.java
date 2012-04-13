package com.atomicleopard.webFramework.mail;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.atomicleopard.expressive.Expressive;

public class MailHttpServletRequest extends HttpServletRequestWrapper {
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Map<String, String[]> parameterMap = new HashMap<String, String[]>();
	private ServletRequest servletRequest;

	@SuppressWarnings("unchecked")
	public MailHttpServletRequest(HttpServletRequest request) {
		super(request);
		System.out.println("Wrapping " + request.getClass());
		// this is a magic value for JASPER, that tells the JspServlet to use this jsp file.
		// setAttribute("org.apache.catalina.jsp_file", requestedResource);
		for (Object attribute : Expressive.iterable(request.getAttributeNames())) {
			attributes.put((String)attribute, request.getAttribute((String)attribute));
		}
		parameterMap.putAll(getParameterMap());
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public void setRequest(ServletRequest request) {
		this.servletRequest = request;
	}

	@Override
	public ServletRequest getRequest() {
		return this.servletRequest;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getParameterMap() {
		return parameterMap;
	}

	@Override
	public String getParameter(String name) {
		String[] entry = parameterMap.get(name);
		return entry == null || entry.length < 1 ? null : entry[0];
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameterMap.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameterMap.get(name);
	}

	private static class EmptyEnumeration implements Enumeration<Object> {
		@Override
		public boolean hasMoreElements() {
			return false;
		}

		@Override
		public Object nextElement() {
			return null;
		}
	};
}
