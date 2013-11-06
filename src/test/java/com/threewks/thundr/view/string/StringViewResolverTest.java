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
package com.threewks.thundr.view.string;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class StringViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private StringViewResolver stringViewResolver = new StringViewResolver();
	private HttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();

	@Test
	public void shouldWriteViewToResponse() {
		stringViewResolver.resolve(req, resp, new StringView("My view result"));
		assertThat(resp.content(), is("My view result"));
		assertThat(resp.isCommitted(), is(true));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentType(), is("text/plain"));
	}

	@Test
	public void shouldWriteNoContentTypeIfNotSet() {
		stringViewResolver.resolve(req, resp, new StringView("My view result").contentType(null));
		assertThat(resp.content(), is("My view result"));
		assertThat(resp.isCommitted(), is(true));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentType(), is(nullValue()));
	}

	@Test
	public void shouldThrowViewResolutionExceptionIfWriteFails() throws IOException {
		thrown.expect(ViewResolutionException.class);
		resp = spy(resp);
		when(resp.getWriter()).thenThrow(new IOException("simulated exception"));
		stringViewResolver.resolve(req, resp, new StringView("My view result"));
	}

	@Test
	public void shouldReturnClassNameForToString() {
		assertThat(new StringViewResolver().toString(), is("StringViewResolver"));
	}

	@Test
	public void shouldApplySpecifiedContentType() {
		stringViewResolver.resolve(req, resp, new StringView("My view result").contentType("text/html"));
		assertThat(resp.content(), is("My view result"));
		assertThat(resp.isCommitted(), is(true));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentType(), is("text/html"));
	}
	
	@Test
	public void shouldUseDifferentCharacterEncodingWhenSpecified() throws UnsupportedEncodingException {
		stringViewResolver = new StringViewResolver("UTF-16");
		stringViewResolver.resolve(req, resp, new StringView("My view result").contentType("text/html"));
		assertThat(resp.getCharacterEncoding(), is("UTF-16"));
		assertThat(resp.isCommitted(), is(true));
		assertThat(resp.getContentType(), is("text/html"));
		assertThat(resp.content(), is("My view result"));
	}
}
