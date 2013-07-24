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

import static com.threewks.thundr.route.RouteType.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RouteTypeTest {

	@Test
	public void shouldReturnAllRouteTypes() {
		assertThat(RouteType.all(), hasItems(GET, POST, PUT, PATCH, DELETE));
	}

	@Test
	public void shouldReturnRouteTypeFromString() {
		assertThat(RouteType.from("GET"), is(GET));
		assertThat(RouteType.from("POST"), is(POST));
		assertThat(RouteType.from("PUT"), is(PUT));
		assertThat(RouteType.from("PATCH"), is(PATCH));
		assertThat(RouteType.from("DELETE"), is(DELETE));
	}

	@Test
	public void shouldReturnRouteTypeFromStringIgnoringCase() {
		assertThat(RouteType.from("get"), is(GET));
		assertThat(RouteType.from("post"), is(POST));
		assertThat(RouteType.from("put"), is(PUT));
		assertThat(RouteType.from("patch"), is(PATCH));
		assertThat(RouteType.from("delete"), is(DELETE));
	}

	@Test
	public void shouldReturnRouteTypeFromStringIgnoringWhitespace() {
		assertThat(RouteType.from(" gEt "), is(GET));
		assertThat(RouteType.from(" pOSt "), is(POST));
		assertThat(RouteType.from(" pUt "), is(PUT));
		assertThat(RouteType.from(" pATCh"), is(PATCH));
		assertThat(RouteType.from(" deLEte\t"), is(DELETE));
	}
}
