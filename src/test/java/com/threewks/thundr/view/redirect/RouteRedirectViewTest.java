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

import org.junit.Test;

import com.atomicleopard.expressive.Expressive;

public class RouteRedirectViewTest {

	@Test
	public void shouldRetainRouteName() {
		RouteRedirectView redirectView = new RouteRedirectView("route_name");
		assertThat(redirectView.getRoute(), is("route_name"));
		assertThat(redirectView.toString(), is("Redirect to route 'route_name'"));
	}

	@Test
	public void shouldRetainPathVariables() {
		RouteRedirectView redirectView = new RouteRedirectView("route_name", Expressive.<String, Object>map("var1", "val1", "var2", "val2"));
		assertThat(redirectView.getPathVariables(), is(Expressive.<String, Object>map("var1", "val1", "var2", "val2")));
	}
}
