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
package com.threewks.thundr.view.jsp;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class SpecificJspViewResolverTest {
	private MockHttpServletRequest mockRequest = new MockHttpServletRequest();
	private MockHttpServletResponse mockResponse = new MockHttpServletResponse();

	@Test
	public void shouldRetainGivenJsp() {
		assertThat(new SpecificJspViewResolver<Object>("/WEB-INF/jsp/something/page.jsp").jspPath(), is("/WEB-INF/jsp/something/page.jsp"));
	}

	@Test
	public void shouldDefaultGivenValueToWebInfJspPath() {
		assertThat(new SpecificJspViewResolver<Object>("something/page.jsp").jspPath(), is("/WEB-INF/jsp/something/page.jsp"));
	}

	@Test
	public void shouldAddViewResultAsRequestAttributeWithNameValue() {
		SpecificJspViewResolver<Object> resolver = new SpecificJspViewResolver<Object>("page.jsp");
		Object viewResult = new Object();
		resolver.resolve(mockRequest, mockResponse, viewResult);
		assertThat(mockRequest.getAttribute("value"), is(viewResult));
	}

	@Test
	public void shouldSetContentTypeAndEncodingOnResponse() {
		SpecificJspViewResolver<Object> resolver = new SpecificJspViewResolver<Object>("page.jsp");
		Object viewResult = new Object();
		resolver.resolve(mockRequest, mockResponse, viewResult);
		assertThat(mockResponse.getContentType(), is("text/html"));
		assertThat(mockResponse.getCharacterEncoding(), is("UTF-8"));
	}

	@Test
	public void shouldIncludeTheSpecifiedJsp() {
		SpecificJspViewResolver<Object> resolver = new SpecificJspViewResolver<Object>("page.jsp");
		Object viewResult = new Object();
		resolver.resolve(mockRequest, mockResponse, viewResult);
		assertThat(mockRequest.getAttribute("value"), is(viewResult));
		assertThat(mockRequest.requestDispatcher().lastPath(), is("/WEB-INF/jsp/page.jsp"));
		assertThat(mockRequest.requestDispatcher().included(), is(true));
	}
}
