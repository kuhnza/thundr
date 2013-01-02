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
package com.threewks.thundr.http.service.gae;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jodd.util.Base64;
import jodd.util.StringUtil;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.FileParameter;
import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.http.service.HttpRequest;
import com.threewks.thundr.http.service.HttpRequestException;
import com.threewks.thundr.http.service.HttpResponse;
import com.threewks.thundr.util.Streams;

public class HttpRequestImpl implements HttpRequest {
	private String encoding = "UTF-8";
	private String url;
	private Object body;
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
	private List<HttpCookie> cookies = new ArrayList<HttpCookie>();
	private boolean followRedirects = true;
	private long timeout = 60000;
	private HttpServiceImpl httpService;
	private String username;
	private String password;
	@SuppressWarnings("unused")
	private String scheme;
	@SuppressWarnings("unused")
	private List<FileParameter> fileParameters = Collections.emptyList();

	public HttpRequestImpl(HttpServiceImpl httpService, String url) {
		this.httpService = httpService;
		this.url = HttpSupport.convertToValidUrl(url);
	}

	/**
	 * Indicate if the WS should continue when hitting a 301 or 302
	 * 
	 * @return the WebRequest for chaining.
	 */
	@Override
	public HttpRequest followRedirects(boolean value) {
		this.followRedirects = value;
		return this;
	}

	@Override
	public HttpRequest timeout(long millis) {
		this.timeout = millis;
		return this;
	}

	@Override
	public HttpRequest body(Object body) {
		this.body = body;
		return this;
	}

	/**
	 * Sets the content type of the request. For content types not supported in the {@link ContentType} enum,
	 * you can set the content type header directly. <code>
	 * <pre>
	 * request.header({@link HttpSupport#HttpHeaderContentType}, "content/type");
	 * </pre>
	 * </code>
	 */
	@Override
	public HttpRequest contentType(ContentType contentType) {
		return contentType(contentType.value());
	}

	@Override
	public HttpRequest contentType(String contentType) {
		header(HttpSupport.Header.ContentType, contentType);
		return this;
	}

	@Override
	public HttpRequest headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	@Override
	public HttpRequest header(String name, String value) {
		this.headers.put(name, value);
		return this;
	}

	@Override
	public HttpRequest cookie(HttpCookie cookie) {
		this.cookies.add(cookie);
		return this;
	}

	@Override
	public HttpRequest cookies(Collection<HttpCookie> cookie) {
		this.cookies.addAll(cookie);
		return this;
	}

	@Override
	public HttpRequest parameter(String name, String value) {
		this.parameters.put(name, value);
		return this;
	}

	@Override
	public HttpRequest parameter(String name, Object value) {
		this.parameters.put(name, value);
		return this;
	}

	/**
	 * Adds the given parameters to request.
	 * GET: parameters are passed as a query string
	 * POST: parameters are passed in the body
	 * PUT: parameters are passed in the body
	 * DELETE: parameters are passed as a query string
	 * HEAD: parameters are passed as a query string
	 */
	@Override
	public HttpRequest parameters(Map<String, Object> parameters) {
		this.parameters.putAll(parameters);
		return this;
	}

	@Override
	public HttpResponse get() {
		return headGetDelete(HTTPMethod.GET);
	}

	@Override
	public HttpResponse post() {
		return postOrPut(HTTPMethod.POST);
	}

	@Override
	public HttpResponse put() {
		return postOrPut(HTTPMethod.PUT);
	}

	@Override
	public HttpResponse delete() {
		return headGetDelete(HTTPMethod.DELETE);
	}

	@Override
	public HttpResponse head() {
		return headGetDelete(HTTPMethod.HEAD);
	}

	/**
	 * define client authentication for a server host
	 * provided credentials will be used during the request
	 * 
	 * @param username
	 * @param password
	 * @return the WebRequest for chaining.
	 */
	public HttpRequest authenticate(String username, String password, String scheme) {
		this.username = username;
		this.password = password;
		this.scheme = scheme;
		return this;
	}

	/**
	 * define client authentication for a server host
	 * provided credentials will be used during the request
	 * the basic scheme will be used
	 * 
	 * @param username
	 * @param password
	 * @return the WebRequest for chaining.
	 */
	public HttpRequest authenticate(String username, String password) {
		return authenticate(username, password, "BASIC");
	}

	public HttpRequest files(FileParameter... fileParameters) {
		this.fileParameters = Arrays.asList(fileParameters);
		return this;
	}

	public HttpRequest files(List<FileParameter> fileParameters) {
		this.fileParameters = fileParameters;
		return this;
	}

	private HttpResponse headGetDelete(HTTPMethod headGetDelete) {

		FetchOptions fetchOptions = createFetchOptions();
		setContentTypeIfNotPresent(ContentType.TextPlain);
		URL requestUrl = buildGetRequestUrl();
		HTTPRequest request = new HTTPRequest(requestUrl, headGetDelete, fetchOptions);
		addHeaders(request);
		addCookies(request);
		addBody(request);

		try {
			return httpService.fetch(request);
		} catch (Exception e) {
			throw new HttpRequestException(e, "Failed to create a %s request: %s", headGetDelete, e.getMessage());
		}
	}

	private HttpResponseImpl postOrPut(HTTPMethod postOrPut) {
		preparePostParameterBody();
		FetchOptions fetchOptions = createFetchOptions();
		setContentTypeIfNotPresent(ContentType.ApplicationFormUrlEncoded);
		URL requestUrl = buildPostRequestUrl();
		HTTPRequest request = new HTTPRequest(requestUrl, postOrPut, fetchOptions);
		addHeaders(request);
		addCookies(request);
		addBody(request);

		try {
			return httpService.fetch(request);
		} catch (Exception e) {
			throw new HttpRequestException(e, "Failed to create a %s request: %s", postOrPut, e.getMessage());
		}
	}

	protected String basicAuthHeader() {
		return "Basic " + Base64.encodeToString(this.username + ":" + this.password);
	}

	protected String encode(String part) {
		try {
			return URLEncoder.encode(part, encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String createQueryString() {
		StringBuilder sb = new StringBuilder();
		for (String key : this.parameters.keySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			Object value = this.parameters.get(key);

			if (value != null) {
				if (value instanceof Collection<?> || value.getClass().isArray()) {
					Collection<?> values = value.getClass().isArray() ? Arrays.asList((Object[]) value) : (Collection<?>) value;
					boolean first = true;
					for (Object v : values) {
						if (!first) {
							sb.append("&");
						}
						first = false;
						sb.append(encode(key)).append("=").append(encode(v.toString()));
					}
				} else {
					sb.append(encode(key)).append("=").append(encode(this.parameters.get(key).toString()));
				}
			}
		}
		return sb.toString();
	}

	private FetchOptions createFetchOptions() {
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		fetchOptions = fetchOptions.setDeadline((double) timeout / (double) 1000);
		return fetchOptions = followRedirects ? fetchOptions.followRedirects() : fetchOptions.doNotFollowRedirects();
	}

	private void addCookies(HTTPRequest request) {
		for (HttpCookie cookie : cookies) {
			request.addHeader(new HTTPHeader("Cookie", cookie.toString()));
		}
	}

	private void addHeaders(HTTPRequest request) {
		for (Map.Entry<String, String> header : headers.entrySet()) {
			request.addHeader(new HTTPHeader(header.getKey(), StringUtil.toString(header.getValue())));
		}
	}

	private void preparePostParameterBody() {
		// if the body has been explicitly set, the consumer is handling data encoding
		if (body == null) {
			List<String> parameterPairs = new ArrayList<String>(this.parameters.size());
			for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
				String pair = String.format("%s=%s", encodeParameter(parameter.getKey()), encodeParameter(StringUtil.toString(parameter.getValue())));
				parameterPairs.add(pair);
			}
			body = StringUtil.join(parameterPairs, "&");
		}
	}

	private void addBody(HTTPRequest request) {
		if (body != null) {
			InputStream is = httpService.convertOutgoing(body);
			request.setPayload(Streams.readBytes(is));
		}
	}

	private void setContentTypeIfNotPresent(ContentType contentType) {
		if (!headers.containsKey(HttpSupport.Header.ContentType)) {
			header(HttpSupport.Header.ContentType, contentType.value());
		}
	}

	private URL buildGetRequestUrl() {
		String requestUrlString = url;
		try {
			String queryString = createQueryString();
			if (StringUtil.isNotBlank(queryString)) {
				requestUrlString = String.format("%s%s%s", requestUrlString, url.contains("?") ? "&" : "?", queryString);
			}
			return new URL(requestUrlString);
		} catch (MalformedURLException e) {
			throw new HttpRequestException(e, "Failed to create destination url - generated '%s': %s", requestUrlString, e.getMessage());
		}
	}

	private URL buildPostRequestUrl() {
		String requestUrlString = url;
		try {
			return new URL(requestUrlString);
		} catch (MalformedURLException e) {
			throw new HttpRequestException(e, "Failed to create destination url - generated '%s': %s", requestUrlString, e.getMessage());
		}
	}

	private String encodeParameter(String value) {
		try {
			return URLEncoder.encode(value, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new HttpRequestException(e, "Unable to format the parameter using %s: %s", encoding, e.getMessage());
		}
	}

	@Override
	public String toString() {
		return url;
	}
	
}
