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
package com.threewks.thundr.view.jsonp;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class JsonpViewResolverTest {

	@Rule public ExpectedException thrown = ExpectedException.none();

	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private JsonpViewResolver resolver = new JsonpViewResolver();

	@Test
	public void shouldResolveByWritingJsonWrappedInFunctionToOutputStream() throws IOException {
		JsonpView viewResult = new JsonpView(map("key", "value"));
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(HttpServletResponse.SC_OK));
		assertThat(resp.content(), is("callback({\"key\":\"value\"});"));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentLength(), is(15));
	}

	@Test
	public void shouldResolveJsonElementByWritingJsonToOutputStreamAsJsonElement() throws IOException {
		JsonElement jsonEl = createJsonElement();
		JsonpView viewResult = new JsonpView(jsonEl);
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(HttpServletResponse.SC_OK));
		assertThat(resp.content(), is("callback({\"key\":\"value\"});"));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentLength(), is(15));
	}

	@Test
	public void shouldNameCallbackMethodBasedOnCalbackParameter() throws IOException {
		req.parameter("callback", "abcdef");
		JsonpView viewResult = new JsonpView(map("key", "value"));
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(HttpServletResponse.SC_OK));
		assertThat(resp.content(), is("abcdef({\"key\":\"value\"});"));
		assertThat(resp.getCharacterEncoding(), is("UTF-8"));
		assertThat(resp.getContentLength(), is(15));
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenFailedToWriteJsonToOutputStream() throws IOException {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to generate JSONP output for object 'string'");

		resp = spy(resp);
		when(resp.getWriter()).thenThrow(new RuntimeException("fail"));
		JsonpView viewResult = new JsonpView("string");
		resolver.resolve(req, resp, viewResult);
	}

	@Test
	public void shouldReturnClassNameForToString() {
		assertThat(new JsonpViewResolver().toString(), is("JsonpViewResolver"));
	}

	@Test
	public void shouldSetJavascriptContentType() {
		JsonpView viewResult = new JsonpView(map("key", "value"));
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.getContentType(), is("application/javascript"));
	}

	private JsonElement createJsonElement() {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson("{\"key\":\"value\"}", JsonElement.class);
	}

}
