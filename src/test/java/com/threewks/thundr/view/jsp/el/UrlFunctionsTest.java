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
package com.threewks.thundr.view.jsp.el;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UrlFunctionsTest {
	@Test
	public void shouldEncodeRequestParameter() {
		assertThat(UrlFunctions.param("cats & dogs"), is("cats%20%26%20dogs"));
		assertThat(UrlFunctions.param(" cats "), is("%20cats%20"));
		assertThat(UrlFunctions.param(null), is(nullValue()));
	}

	@Test
	public void shouldEncodePathParameter() {
		assertThat(UrlFunctions.path("~path + component's"), is("~path%20%2B%20component's"));
		assertThat(UrlFunctions.path(" cats "), is("%20cats%20"));
		assertThat(UrlFunctions.path(null), is(nullValue()));
	}

	@Test
	public void shouldCreatePathSlugFromPathString() {
		assertThat(UrlFunctions.pathSlug("~path + component's"), is("-path-components"));
		assertThat(UrlFunctions.pathSlug(" cats "), is("-cats-"));
		assertThat(UrlFunctions.pathSlug(null), is(nullValue()));
	}
}
