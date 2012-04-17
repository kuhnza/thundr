package com.atomicleopard.webFramework.test.mock.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class MockHttpSession implements HttpSession {
	private String id = UUID.randomUUID().toString();
	private ServletContext context;
	private Map<String, Object> attributes = new HashMap<String, Object>();

	public MockHttpSession() {
		this(new MockServletContext());
	}

	public MockHttpSession(ServletContext context) {
		this(context, new HashMap<String, Object>());
	}

	public MockHttpSession(Map<String, Object> attributes) {
		this(new MockServletContext(), attributes);
	}

	public MockHttpSession(ServletContext context, Map<String, Object> attributes) {
		this.context = context;
		this.attributes = attributes;
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return context;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
	}

	@Override
	public int getMaxInactiveInterval() {
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Object getValue(String name) {
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributes.keySet());
	}

	@Override
	public String[] getValueNames() {
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public void removeValue(String name) {

	}

	@Override
	public void invalidate() {

	}

	@Override
	public boolean isNew() {
		return false;
	}
}
