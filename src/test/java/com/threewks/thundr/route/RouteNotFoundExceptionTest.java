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
package com.threewks.thundr.route;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class RouteNotFoundExceptionTest {
	@Test
	public void shouldFormatAndRetainReason() {
		RouteNotFoundException routeNotFoundException = new RouteNotFoundException("String %s", "format");
		assertThat(routeNotFoundException.getMessage(), is("String format"));
		assertThat(routeNotFoundException.getCause(), is(nullValue()));
		assertThat(routeNotFoundException.getStatus(), is(HttpServletResponse.SC_NOT_FOUND));
	}

	@Test
	public void shouldFormatAndRetainCauseAndReason() {
		Exception exception = new Exception("cause");
		RouteNotFoundException routeNotFoundException = new RouteNotFoundException(exception, "String %s", "format");
		assertThat(routeNotFoundException.getMessage(), is("String format"));
		assertThat(routeNotFoundException.getCause(), is((Throwable) exception));
		assertThat(routeNotFoundException.getStatus(), is(HttpServletResponse.SC_NOT_FOUND));
	}
}
