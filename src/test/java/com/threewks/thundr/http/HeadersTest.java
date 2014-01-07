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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;

public class HeadersTest {

	@Test
	public void shouldReturnHeaderFromReq() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.header("header-1", "value1");
		assertThat(Headers.getHeader("header-1", req), is("value1"));
	}

	@Test
	public void shouldFirstReturnHeaderFromReqWhenMultipleValuesPresent() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.header("header-1", "value1", "value2");
		assertThat(Headers.getHeader("header-1", req), is("value1"));
	}

	@Test
	public void shouldReturnNullWhenHeaderNotPresent() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		assertThat(Headers.getHeader("header-1", req), is(nullValue()));
	}

	@Test
	public void shouldReturnAllHeaderValuesInAList() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.header("header-1", "value1", "value2");
		assertThat(Headers.getHeaders("header-1", req), hasItems("value1", "value2"));
	}

	@Test
	public void shouldReturnAnEmptyCollectionWhenHeaderIsNotPresent() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		assertThat(Headers.getHeaders("header-1", req).isEmpty(), is(true));
	}

	@Test
	public void shouldReturnAllHeaders() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.header("header-1", "value1", "value2");
		req.header("header-2", "value3", "value4");
		req.header("header-3", "value5");
		Map<String, List<String>> headerMap = Headers.getHeaderMap(req);
		assertThat(headerMap, hasEntry("header-1", Arrays.asList("value1", "value2")));
		assertThat(headerMap, hasEntry("header-2", Arrays.asList("value3", "value4")));
		assertThat(headerMap, hasEntry("header-3", Arrays.asList("value5")));
	}

	@Test
	public void shouldReturnAnEmptyMapWhenNoHeadersPresent() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		Map<String, List<String>> headerMap = Headers.getHeaderMap(req);
		assertThat(headerMap, is(notNullValue()));
		assertThat(headerMap.isEmpty(), is(true));
	}
}
