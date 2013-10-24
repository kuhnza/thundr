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
package com.threewks.thundr.http;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;

public class Cookies {

	/**
	 * Gets the first cookie of the given name from the request
	 * 
	 * @param name
	 * @param req
	 * @return
	 */
	public static final Cookie getCookie(String name, HttpServletRequest req) {
		if (req.getCookies() != null) {
			for (Cookie cookie : req.getCookies()) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * Gets all cookies of the given name from the request
	 * 
	 * @param name
	 * @param req
	 * @return
	 */
	public static final List<Cookie> getCookies(String name, HttpServletRequest req) {
		List<Cookie> results = new ArrayList<Cookie>();
		if (req.getCookies() != null) {
			for (Cookie cookie : req.getCookies()) {
				if (cookie.getName().equals(name)) {
					results.add(cookie);
				}
			}
		}
		return results;
	}

	/**
	 * Creates a CookieBuilder, which provides a fluent api for building a {@link Cookie}.
	 * 
	 * @param name
	 * @return
	 */
	public static final CookieBuilder build(String name) {
		return new CookieBuilder(name);
	}

	public static class CookieBuilder {
		private String name;
		private String value;
		private String path;
		private String domain;
		private Duration expires;
		private String comment;
		private Integer version;
		private Boolean secure;

		public CookieBuilder() {

		}

		public CookieBuilder(String name) {
			this.name = name;
		}

		public CookieBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public CookieBuilder withValue(String value) {
			this.value = value;
			return this;
		}

		public CookieBuilder withPath(String path) {
			this.path = path;
			return this;
		}

		public CookieBuilder withDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public CookieBuilder withExpires(Duration expires) {
			this.expires = expires;
			return this;
		}

		public CookieBuilder withVersion(Integer version) {
			this.version = version;
			return this;
		}

		public CookieBuilder withSecure(Boolean secure) {
			this.secure = secure;
			return this;
		}

		public CookieBuilder withComment(String comment) {
			this.comment = comment;
			return this;
		}

		public Cookie build() {
			Cookie cookie = new Cookie(name, value);
			if (domain != null) {
				cookie.setDomain(domain);
			}
			if (path != null) {
				cookie.setPath(path);
			}
			if (expires != null) {
				cookie.setMaxAge((int) expires.getStandardSeconds());
			}
			if (comment != null) {
				cookie.setComment(comment);
			}
			if (version != null) {
				cookie.setVersion(version);
			}
			if (secure != null) {
				cookie.setSecure(secure);
			}
			return cookie;
		}

		public String toString() {
			String domainAndPath = trimToEmpty(domain) + trimToEmpty(path);
			domainAndPath = StringUtils.isEmpty(domainAndPath) ? "" : " (" + domainAndPath + ")";
			String expires = this.expires == null ? "" : " expires " + this.expires.getStandardSeconds() + "s";
			return String.format("%s=%s%s%s%s%s;", name, value, Boolean.TRUE.equals(secure) ? " [secure]" : "", domainAndPath, expires, version == null ? "" : " v" + version, trimToEmpty(comment));
		}
	}
}
