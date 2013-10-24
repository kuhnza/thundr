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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.Duration;
import org.junit.Test;

import com.threewks.thundr.http.Cookies.CookieBuilder;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;

public class CookiesTest {

	@Test
	public void shouldGetNamedCookieFromRequest() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.cookie("cookie2", "value");
		req.cookie("cookie", "value");

		Cookie cookie = Cookies.getCookie("cookie", req);
		assertThat(cookie, is(notNullValue()));
		assertThat(cookie.getValue(), is("value"));
	}

	@Test
	public void shouldGetNullWhenNoNamedCookieExistsInRequest() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		Cookie cookie = Cookies.getCookie("cookie", req);
		assertThat(cookie, is(nullValue()));
	}

	@Test
	public void shouldGetNullWhenNoCookiesInRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class);
		Cookie cookie = Cookies.getCookie("cookie", req);
		assertThat(cookie, is(nullValue()));
	}

	@Test
	public void shouldGetFirstNamedCookieFromRequest() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.cookie("cookie2", "value");
		req.cookie("cookie", "value");
		req.cookie("cookie", "value2");
		req.cookie("cookie", "value3");

		Cookie cookie = Cookies.getCookie("cookie", req);
		assertThat(cookie, is(notNullValue()));
		assertThat(cookie.getValue(), is("value"));
	}

	@Test
	public void shouldGetAllNamedCookiesFromRequest() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.cookie("cookie", "value");
		req.cookie("cookie", "value2");
		req.cookie("cookie", "value3");
		req.cookie("cookie2", "value3");

		List<Cookie> cookies = Cookies.getCookies("cookie", req);
		assertThat(cookies.size(), is(3));
		assertThat(cookies.get(0).getValue(), is("value"));
		assertThat(cookies.get(1).getValue(), is("value2"));
		assertThat(cookies.get(2).getValue(), is("value3"));
	}

	@Test
	public void shouldGetEmptyListWhenNoNamedCookieExistsInRequest() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		List<Cookie> cookies = Cookies.getCookies("cookie", req);
		assertThat(cookies, is(notNullValue()));
		assertThat(cookies.isEmpty(), is(true));
	}

	@Test
	public void shouldGetEmptyListWhenNoCookiesInRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class);
		List<Cookie> cookies = Cookies.getCookies("cookie", req);
		assertThat(cookies, is(notNullValue()));
		assertThat(cookies.isEmpty(), is(true));
	}

	@Test
	public void shouldBuildABasicCookie() {
		Cookie cookie = Cookies.build("name").withValue("value").build();
		assertThat(cookie.getName(), is("name"));
		assertThat(cookie.getValue(), is("value"));
		assertThat(cookie.getVersion(), is(0));
		assertThat(cookie.getComment(), is(nullValue()));
		assertThat(cookie.getDomain(), is(nullValue()));
		assertThat(cookie.getPath(), is(nullValue()));
		assertThat(cookie.getSecure(), is(false));
		assertThat(cookie.getMaxAge(), is(-1));
	}

	@Test
	public void shouldHaveDefaultCtorAndWithName() {
		Cookie cookie = new CookieBuilder().withName("name").build();
		assertThat(cookie.getName(), is("name"));
	}

	@Test
	public void shouldBuildAFullCookie() {
		CookieBuilder builder = Cookies.build("name");
		assertThat(builder.withValue("value"), is(builder));
		assertThat(builder.withDomain("domain.com"), is(builder));
		assertThat(builder.withPath("/path/."), is(builder));
		assertThat(builder.withSecure(true), is(builder));
		assertThat(builder.withExpires(new Duration(1234)), is(builder));
		assertThat(builder.withVersion(2), is(builder));
		assertThat(builder.withComment("Comment"), is(builder));

		Cookie cookie = builder.build();
		assertThat(cookie.getName(), is("name"));
		assertThat(cookie.getValue(), is("value"));
		assertThat(cookie.getVersion(), is(2));
		assertThat(cookie.getComment(), is("Comment"));
		assertThat(cookie.getDomain(), is("domain.com"));
		assertThat(cookie.getPath(), is("/path/."));
		assertThat(cookie.getSecure(), is(true));
	}

	@Test
	public void shouldHaveUsefulToString() {
		CookieBuilder builder = Cookies.build("name");
		assertThat(builder.toString(), is("name=null;"));

		builder.withValue("value");
		assertThat(builder.toString(), is("name=value;"));

		builder.withDomain("domain.com");
		builder.withPath("/path/.");

		assertThat(builder.toString(), is("name=value (domain.com/path/.);"));

		builder.withSecure(true);

		assertThat(builder.toString(), is("name=value [secure] (domain.com/path/.);"));

		builder.withExpires(new Duration(30000));
		builder.withVersion(2);
		builder.withComment("Comment");

		assertThat(builder.toString(), is("name=value [secure] (domain.com/path/.) expires 30s v2;"));
	}
}
