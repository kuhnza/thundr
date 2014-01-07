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

import static com.atomicleopard.expressive.Expressive.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jodd.io.StringInputStream;
import jodd.io.StringInputStream.Mode;

import com.atomicleopard.expressive.Cast;
import com.threewks.thundr.http.ContentType;

@SuppressWarnings("rawtypes")
public class MockHttpServletRequest implements HttpServletRequest {
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Map<String, String[]> parameters = new HashMap<String, String[]>();
	private Map<String, String[]> headers = new HashMap<String, String[]>();
	private String characterEncoding = "utf-8";
	private String contentType = null;
	private String protocol = "http";
	private String method = "GET";
	private String path = "/";
	private String queryString = "";
	private HttpSession session;
	private String content;
	private RequestDispatcher requestDispatcher = new MockRequestDispatcher();
	private String serverName;
	private List<Cookie> cookies = list();

	public MockHttpServletRequest() {
	}

	public MockHttpServletRequest(String url) {
		url(url);
	}

	public MockHttpServletRequest content(String content) {
		this.content = content;
		return this;
	}

	public MockHttpServletRequest method(String method) {
		this.method = method;
		return this;
	}

	public MockHttpServletRequest url(String url) {
		Pattern pattern = Pattern.compile("^((.+):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$");
		Matcher matcher = pattern.matcher(url);
		if (!matcher.matches()) {
			path = url;
		} else {
			this.protocol = matcher.group(2);
			this.path = matcher.group(4) + matcher.group(6);
			this.queryString = matcher.group(7);
		}
		return this;
	}

	public MockHttpServletRequest session(HttpSession session) {
		this.session = session;
		return this;
	}

	public MockHttpServletRequest attribute(String name, Object value) {
		attributes.put(name, value);
		return this;
	}

	public MockHttpServletRequest attributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
		return this;
	}

	public MockHttpServletRequest parameter(String name, String value) {
		parameters.put(name, new String[] { value });
		return this;
	}

	public MockHttpServletRequest parameter(String name, String... values) {
		parameters.put(name, values);
		return this;
	}

	public MockHttpServletRequest parameters(Map<String, String[]> parameters) {
		this.parameters.putAll(parameters);
		return this;
	}

	public MockHttpServletRequest header(String name, String value) {
		headers.put(name, new String[] { value });
		return this;
	}

	public MockHttpServletRequest header(String name, String... values) {
		headers.put(name, values);
		return this;
	}

	public MockHttpServletRequest header(Map<String, String[]> headers) {
		this.headers.putAll(parameters);
		return this;
	}

	public MockHttpServletRequest contentType(ContentType contentType) {
		this.contentType = contentType.value();
		return this;
	}

	public MockHttpServletRequest contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public MockHttpServletRequest encoding(String encoding) {
		this.characterEncoding = encoding;
		return this;
	}

	public MockHttpServletRequest serverName(String serverName) {
		this.serverName = serverName;
		return this;
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
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		this.characterEncoding = env;
	}

	@Override
	public int getContentLength() {
		return this.content == null ? 0 : content.getBytes().length;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@SuppressWarnings("resource")
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final StringInputStream sis = new StringInputStream(content, Mode.ALL);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return sis.read();
			}
		};
	}

	@Override
	public String getParameter(String name) {
		String[] parameterArray = parameters.get(name);
		return parameterArray != null && parameterArray.length > 0 ? parameterArray[0] : null;
	}

	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameters.get(name);
	}

	@Override
	public Map getParameterMap() {
		return parameters;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public int getServerPort() {
		return 80;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public void setAttribute(String name, Object o) {
		attributes.put(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public Enumeration getLocales() {
		return Collections.enumeration(Collections.emptyList());
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		if (requestDispatcher() != null) {
			requestDispatcher().lastPath(path);
		}
		return requestDispatcher;
	}

	public void requestDispatcher(RequestDispatcher requestDispatcher) {
		this.requestDispatcher = requestDispatcher;
	}

	public MockRequestDispatcher requestDispatcher() {
		return Cast.as(requestDispatcher, MockRequestDispatcher.class);
	}

	@Override
	public String getRealPath(String path) {
		return path;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 80;
	}

	@Override
	public String getAuthType() {
		return null;
	}

	public void cookie(Cookie cookie) {
		cookies.add(cookie);
	}

	public void cookie(String name, String value) {
		cookies.add(new Cookie(name, value));
	}

	public String cookie(String name) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public void clearCookies() {
		cookies.clear();
	}

	@Override
	public Cookie[] getCookies() {
		return cookies.toArray(new Cookie[0]);
	}

	@Override
	public long getDateHeader(String name) {
		return 0;
	}

	@Override
	public String getHeader(String name) {
		String[] values = headers.get(name);
		return values == null ? null : values[0];
	}

	@Override
	public Enumeration getHeaders(String name) {
		String[] values = headers.get(name);
		return values == null ? null : Collections.enumeration(list(values));
	}

	@Override
	public Enumeration getHeaderNames() {
		return Collections.enumeration(headers.keySet());
	}

	@Override
	public int getIntHeader(String name) {
		return 0;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPathInfo() {
		return path;
	}

	@Override
	public String getPathTranslated() {
		return path;
	}

	@Override
	public String getContextPath() {
		return "";
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return path;
	}

	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer(path);
	}

	@Override
	public String getServletPath() {
		return "/";
	}

	@Override
	public HttpSession getSession(boolean create) {
		return session == null && create ? new MockHttpSession() : session;
	}

	@Override
	public HttpSession getSession() {
		return session;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}
}
