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

import static com.atomicleopard.expressive.Expressive.list;
import static com.threewks.thundr.route.RouteType.GET;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.threewks.thundr.action.Action;
import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;

public class RoutesTest {
	@Test
	public void shouldParseDeepWildcardRoutes() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"**\": \"static\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "static", GET, String.format("[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseWildcardRoutes() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"*\": \"static\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "static", GET, String.format("[%s]*?", Route.AcceptablePathCharacters));
	}

	@Test
	public void shouldParseResourceRoutes() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"/*.jpg\": \"static\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "static", GET, String.format("/[%s]*?.jpg", Route.AcceptablePathCharacters));
	}

	@Test
	public void shouldParseGetRouteType() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"/base/**\": { \"GET\": \"some.java.Class.method\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "some.java.Class.method", RouteType.GET, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParsePostRouteType() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"/base/**\": { \"POST\": \"some.java.Class.method\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "some.java.Class.method", RouteType.POST, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParsePutRouteType() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"/base/**\": {\"PUT\": \"some.java.Class.method\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "some.java.Class.method", RouteType.PUT, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseDeleteRouteType() {
		List<Route> routes = Routes.parseJsonRoutes("{ \"/base/**\": {\"DELETE\": \"some.java.Class.method\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "some.java.Class.method", RouteType.DELETE, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMultipleRoutes() {
		String json = "{" + "\"/route1/**\": \"some.java.Class.method1\"," + "\"/route2/**\": {\"PUT\": \"some.java.Class.method2\"}," + "\"/route3/**\": {\"POST\": \"some.java.Class.method3\"}"
				+ "}";
		List<Route> routes = Routes.parseJsonRoutes(json);
		assertThat(routes.size(), is(3));
		assertRoute(routes, 0, "some.java.Class.method1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "some.java.Class.method2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "some.java.Class.method3", RouteType.POST, String.format("/route3/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMixedCaseRouteTypes() {
		String json = "{" + "\"/route1/**\": {\"get\": \"some.java.Class.method1\"}," + "\"/route2/**\": {\"pUt\": \"some.java.Class.method2\"},"
				+ "\"/route3/**\": {\"post\": \"some.java.Class.method3\"}," + "\"/route4/**\": {\"deLEte\": \"some.java.Class.method4\"}" + "}";
		List<Route> routes = Routes.parseJsonRoutes(json);
		assertThat(routes.size(), is(4));
		assertRoute(routes, 0, "some.java.Class.method1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "some.java.Class.method2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "some.java.Class.method3", RouteType.POST, String.format("/route3/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 3, "some.java.Class.method4", RouteType.DELETE, String.format("/route4/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMultipleRouteTypesPerEntryCaseRouteTypes() {
		String json = "{" + "\"/route1/**\": {\"GET\": \"some.java.Class.method1\", \"PUT\": \"some.java.Class.method1\"},"
				+ "\"/route2/**\": {\"PUT\": \"some.java.Class.method2\", \"DELETE\": \"some.java.Class.method3\"}" + "}";
		List<Route> routes = Routes.parseJsonRoutes(json);
		assertThat(routes.size(), is(4));
		assertRoute(routes, 0, "some.java.Class.method1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "some.java.Class.method1", RouteType.PUT, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "some.java.Class.method2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 3, "some.java.Class.method3", RouteType.DELETE, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldHaveRouteAfterAddingRoutes() {
		Routes routes = new Routes();
		routes.addActionResolver(TestAction.class, new TestActionResolver());
		Route route1 = new Route("/path/*.jpg", "static", RouteType.GET);
		routes.addRoutes(list(route1));

		assertThat(routes.findMatchingRoute("/path/image.jpg", RouteType.GET), is(route1));
		assertThat(routes.findMatchingRoute("/path/image.jpg", RouteType.POST), is(nullValue()));
		assertThat(routes.findMatchingRoute("/path/image.jpeg", RouteType.GET), is(nullValue()));
	}

	@Test
	public void shouldReturnTrueIfNoRoutesHaveBeenAdded() {
		Routes routes = new Routes();
		routes.addActionResolver(TestAction.class, new TestActionResolver());
		assertThat(routes.isEmpty(), is(true));
		routes.addRoute(new Route("/", "static", RouteType.GET));
		assertThat(routes.isEmpty(), is(false));
	}

	private void assertRoute(List<Route> routes, int index, String actionName, RouteType routeType, String path) {
		assertThat(routes.get(index).getActionName(), is(actionName));
		assertThat(routes.get(index).getRouteMatchRegex(), is(path + ("(?:;.*?)*")));
		assertThat(routes.get(index).getRouteType(), is(routeType));
	}

	private static class TestAction implements Action {

	}

	private static class TestActionResolver implements ActionResolver<TestAction> {
		@Override
		public TestAction resolve(TestAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException {
			return action;
		}

		@Override
		public TestAction createActionIfPossible(String actionName) {
			return new TestAction();
		}

	}
}
