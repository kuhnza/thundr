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
package com.threewks.thundr.view.redirect;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.view.ViewResolutionException;

public class RedirectViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private RedirectViewResolver resolver = new RedirectViewResolver();
	private HttpServletRequest req = new MockHttpServletRequest();
	private HttpServletResponse resp = mock(HttpServletResponse.class);

	@Test
	public void shouldRedirectSpecifiedByRedirectView() throws IOException {
		RedirectView viewResult = new RedirectView("/redirect/to");
		resolver.resolve(req, resp, viewResult);
		verify(resp).sendRedirect("/redirect/to");
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenRedirectFails() throws IOException {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to redirect to /redirect/to: BOOM");

		doThrow(new IOException("BOOM")).when(resp).sendRedirect(anyString());

		RedirectView viewResult = new RedirectView("/redirect/to");
		resolver.resolve(req, resp, viewResult);
	}

	@Test
	public void shouldReturnClassNameForToString() {
		assertThat(new RedirectViewResolver().toString(), is("RedirectViewResolver"));

	}
}
