package com.threewks.thundr.http.service.gae;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.atomicleopard.expressive.Expressive;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.threewks.thundr.http.HttpSupport.Header;
import com.threewks.thundr.http.service.HttpRequestException;
import com.threewks.thundr.http.service.HttpResponse;
import com.threewks.thundr.http.service.HttpResponseException;
import com.threewks.thundr.http.service.HttpService;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.profiler.Profiler;

public class HttpResponseImpl implements HttpResponse {
	private Future<HTTPResponse> future;
	private HTTPResponse response;
	private Map<String, List<String>> headers;
	private HttpService service;
	private Map<String, List<HttpCookie>> cookies;
	private Profiler profiler;

	public HttpResponseImpl(Future<HTTPResponse> future, HttpService service) {
		this.future = future;
		this.service = service;
		this.profiler = profiler == null ? Profiler.None : profiler;
	}

	@Override
	public int getStatus() {
		return response().getResponseCode();
	}

	@Override
	public String getContentType() {
		return getHeader(Header.ContentType);
	}

	@Override
	public String getHeader(String name) {
		List<String> headers = getHeaders().get(name);
		return headers == null ? null : headers.get(0);
	}

	@Override
	public List<String> getHeaders(String name) {
		return getHeaders().get(name);
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		response();
		return Collections.unmodifiableMap(headers);
	}

	@Override
	public HttpCookie getCookie(String cookieName) {
		List<HttpCookie> cookies = getCookies(cookieName);
		return cookies == null ? null : cookies.get(0);
	}

	@Override
	public List<HttpCookie> getCookies(String name) {
		response();
		return cookies.get(name);
	}

	@Override
	public List<HttpCookie> getCookies() {
		response();
		return Expressive.flatten(cookies.values());
	}

	@Override
	public String getBody() {
		return getBody(String.class);
	}

	@Override
	public <T> T getBody(Class<T> as) {
		return service.convertIncoming(getBodyAsStream(), as);
	}

	@Override
	public byte[] getBodyAsBytes() {
		return response().getContent();
	}

	@Override
	public InputStream getBodyAsStream() {
		return new ByteArrayInputStream(response().getContent());
	}

	@Override
	public URI getUri() {
		try {
			return response().getFinalUrl().toURI();
		} catch (URISyntaxException e) {
			throw new HttpResponseException(e, "Uri cannot be parsed: %s", e.getMessage());
		}
	}

	private HTTPResponse response() {
		if (response == null) {
			try {
				response = future.get();
				headers = buildHeaderMap();
				cookies = buildCookieMap();
				return response;
			} catch (InterruptedException e) {
				throw new HttpRequestException("Failed to wait for completion of asynchronous request: %s", e.getMessage());
			} catch (ExecutionException e) {
				throw new HttpRequestException(e, "Failed to get result for asynchronous request: %s", e.getMessage());
			}
		}
		return response;
	}

	private Map<String, List<String>> buildHeaderMap() {
		Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
		for (HTTPHeader header : response.getHeaders()) {
			String key = header.getName();
			String value = header.getValue();
			List<String> values = headers.get(key);
			if (values == null) {
				values = new ArrayList<String>();
				headers.put(key, values);
			}
			values.add(value);
		}
		return headers;
	}

	private Map<String, List<HttpCookie>> buildCookieMap() {
		Map<String, List<HttpCookie>> cookies = new LinkedHashMap<String, List<HttpCookie>>();
		for (String setCookieHeader : getCookieHeaders(headers)) {
			List<HttpCookie> cookieSet = parseCookies(setCookieHeader);
			for (HttpCookie httpCookie : cookieSet) {
				String name = httpCookie.getName();
				List<HttpCookie> existingCookies = cookies.get(name);
				if (existingCookies == null) {
					existingCookies = new ArrayList<HttpCookie>();
					cookies.put(name, existingCookies);
				}
				existingCookies.add(httpCookie);
			}
		}
		return cookies;
	}

	/**
	 * Get all cookie headers from the given header map.
	 * 
	 * Note: this will get all "Set-Cookie" and "Set-Cookie2" headers.
	 * 
	 * @param headers the map of headers to get the set cookie headers from.
	 * @return a list of headers.
	 */
	static List<String> getCookieHeaders(Map<String, List<String>> headers) {
		List<String> cookieHeaders = new ArrayList<String>();
		List<String> setCookieHeaders = headers.get(Header.SetCookie);
		if (setCookieHeaders != null) {
			cookieHeaders.addAll(setCookieHeaders);
		}
		List<String> setCookie2Headers = headers.get(Header.SetCookie2);
		if (setCookie2Headers != null) {
			cookieHeaders.addAll(setCookie2Headers);
		}
		return cookieHeaders;
	}

	/**
	 * Safely parse a cookie header. If any exceptions are encountered then the exception is caught
	 * and an empty list is returned.
	 * 
	 * @param setCookieHeader the header to parse.
	 * @return a list of headers, or an empty list if anything goes wrong parsing the header.
	 */
	static List<HttpCookie> parseCookies(String setCookieHeader) {
		List<HttpCookie> cookies = new ArrayList<HttpCookie>();
		try {
			cookies = HttpCookie.parse(setCookieHeader);
		} catch (Exception e) {
			Logger.warn("Unable to parse cookie from header: %s", setCookieHeader);
		}
		return cookies;
	}
}
