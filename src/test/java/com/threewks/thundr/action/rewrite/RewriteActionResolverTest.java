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
package com.threewks.thundr.action.rewrite;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.route.RouteType;
import com.threewks.thundr.route.Routes;

public class RewriteActionResolverTest {
	private RewriteActionResolver resolver;
	private Routes routes;

	@Before
	public void before() {
		routes = mock(Routes.class);
		resolver = new RewriteActionResolver(routes);
	}

	@Test
	public void shouldResolveRewriteAction() {
		Map<String, String> pathVars = map("to", "to", "else", "else");
		RewriteAction action = resolver.createActionIfPossible("rewrite:/something/{to}/something/{else}/");
		assertThat(action, is(notNullValue()));
		assertThat(action.getRewriteTo(pathVars), is("/something/to/something/else/"));
	}
	
	@Test
	public void shouldResolveRewriteActionRegardlessOfCase() {
		Map<String, String> pathVars = map("to", "to", "else", "else");
		RewriteAction action = resolver.createActionIfPossible("reWRIte:/something/{to}/something/{else}/");
		assertThat(action, is(notNullValue()));
		assertThat(action.getRewriteTo(pathVars), is("/something/to/something/else/"));
	}

	@Test
	public void shouldNotResolveRewriteAction() {
		RewriteAction action = resolver.createActionIfPossible("somethingelse:/something/{to}/something/{else}/");
		assertThat(action, is(nullValue()));
	}

	@Test
	public void shouldInvokeRoutesToPerformRewriteAction() {
		RewriteAction action = new RewriteAction("/rewrite/{to}");
		RouteType routeType = RouteType.POST;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		Map<String, String> pathVars = map("to", "new");
		resolver.resolve(action, routeType, req, resp, pathVars);

		verify(routes).invoke("/rewrite/new", routeType, req, resp);
	}
}
