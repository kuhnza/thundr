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

import static com.atomicleopard.expressive.Expressive.*;
import static com.threewks.thundr.route.RouteType.GET;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.Action;
import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;

public class RoutesTest {
	@Rule public ExpectedException thrown = ExpectedException.none();
	private Routes routesObj;

	@Before
	public void before() {
		routesObj = new Routes();
		routesObj.addActionResolver(TestAction.class, new TestActionResolver());
	}

	@Test
	public void shouldParseDeepWildcardRoutes() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"**\": \"test:route\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", GET, String.format("[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseWildcardRoutes() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"*\": \"test:route\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", GET, String.format("[%s]*?", Route.AcceptablePathCharacters));
	}

	@Test
	public void shouldParseResourceRoutes() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"/*.jpg\": \"test:route\"}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", GET, String.format("/[%s]*?.jpg", Route.AcceptablePathCharacters));
	}

	@Test
	public void shouldParseGetRouteType() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"/base/**\": { \"GET\": \"test:route\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", RouteType.GET, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParsePostRouteType() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"/base/**\": { \"POST\": \"test:route\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", RouteType.POST, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParsePutRouteType() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"/base/**\": {\"PUT\": \"test:route\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", RouteType.PUT, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseDeleteRouteType() {
		Map<Route, Action> routes = routesObj.parseJsonRoutes("{ \"/base/**\": {\"DELETE\": \"test:route\"}}");
		assertThat(routes.size(), is(1));
		assertRoute(routes, 0, "test:route", RouteType.DELETE, String.format("/base/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMultipleRoutes() {
		String json = "{" + "\"/route1/**\": \"test:route1\"," + "\"/route2/**\": {\"PUT\": \"test:route2\"}," + "\"/route3/**\": {\"POST\": \"test:route3\"}" + "}";
		Map<Route, Action> routes = routesObj.parseJsonRoutes(json);
		assertThat(routes.size(), is(3));
		assertRoute(routes, 0, "test:route1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "test:route2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "test:route3", RouteType.POST, String.format("/route3/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMixedCaseRouteTypes() {
		String json = "{" + "\"/route1/**\": {\"get\": \"test:route1\"}," + "\"/route2/**\": {\"pUt\": \"test:route2\"}," + "\"/route3/**\": {\"post\": \"test:route3\"},"
				+ "\"/route4/**\": {\"deLEte\": \"test:route4\"}" + "}";
		Map<Route, Action> routes = routesObj.parseJsonRoutes(json);
		assertThat(routes.size(), is(4));
		assertRoute(routes, 0, "test:route1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "test:route2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "test:route3", RouteType.POST, String.format("/route3/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 3, "test:route4", RouteType.DELETE, String.format("/route4/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldParseMultipleRouteTypesPerEntryCaseRouteTypes() {
		String json = "{" + "\"/route1/**\": {\"GET\": \"test:route1\", \"PUT\": \"test:route1\"}," + "\"/route2/**\": {\"PUT\": \"test:route2\", \"DELETE\": \"test:route3\"}" + "}";
		Map<Route, Action> routes = routesObj.parseJsonRoutes(json);
		assertThat(routes.size(), is(4));
		assertRoute(routes, 0, "test:route1", RouteType.GET, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 1, "test:route1", RouteType.PUT, String.format("/route1/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 2, "test:route2", RouteType.PUT, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertRoute(routes, 3, "test:route3", RouteType.DELETE, String.format("/route2/[%s]*?", Route.AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldHaveRouteAfterAddingRoutes() {
		Routes routes = new Routes();
		routes.addActionResolver(TestAction.class, new TestActionResolver());
		Route route1 = new Route(RouteType.GET, "/path/*.jpg", null);
		Map<Route, Action> routeMap = Expressive.map(route1, new TestAction("action"));
		routes.addRoutes(routeMap);

		assertThat(routes.findMatchingRoute("/path/image.jpg", RouteType.GET), is(route1));
		assertThat(routes.findMatchingRoute("/path/image.jpg", RouteType.POST), is(nullValue()));
		assertThat(routes.findMatchingRoute("/path/image.jpeg", RouteType.GET), is(nullValue()));
	}

	@Test
	public void shouldReturnTrueIfNoRoutesHaveBeenAdded() {
		Routes routes = new Routes();
		routes.addActionResolver(TestAction.class, new TestActionResolver());
		assertThat(routes.isEmpty(), is(true));
		routes.addRoute(new Route(RouteType.GET, "/", null), null);
		assertThat(routes.isEmpty(), is(false));
	}

	private void assertRoute(Map<Route, Action> routes, int index, String actionName, RouteType routeType, String path) {
		Route route = new ArrayList<Route>(routes.keySet()).get(index);
		assertThat(route.getRouteMatchRegex(), is(path + ("(?:;.*?)*")));
		assertThat(route.getRouteType(), is(routeType));
		Action action = routes.get(route);
		assertThat(action.toString(), is(actionName));
	}

	@Test
	public void shouldThrowRouteExceptionWhenAddingRouteWithNameThatIsAlreadyTaken() {
		thrown.expect(RouteException.class);
		thrown.expectMessage("Unable to add the route 'GET /route2/' with the name 'name' - the route 'GET /route/' has already been registered with this name");
		routesObj.addRoute(RouteType.GET, "/route/", "name", new TestAction("action"));
		routesObj.addRoute(RouteType.GET, "/route2/", "name", new TestAction("action"));
	}

	@Test
	public void shouldThrowRouteExceptionWhenAddingRouteWithSamePattern() {
		thrown.expect(RouteException.class);
		thrown.expectMessage("Unable to add the route 'GET /route/{var2}' - the route 'GET /route/{var}' already exists which matches the same pattern");
		routesObj.addRoute(RouteType.GET, "/route/{var}", null, new TestAction("action"));
		routesObj.addRoute(RouteType.GET, "/route/{var2}", null, new TestAction("action"));
	}

	private static class TestAction implements Action {

		private String actionName;

		public TestAction(String actionName) {
			this.actionName = actionName;
		}

		@Override
		public String toString() {
			return actionName;
		}

	}

	private static class TestActionResolver implements ActionResolver<TestAction> {
		@Override
		public TestAction resolve(TestAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException {
			return action;
		}

		@Override
		public TestAction createActionIfPossible(String actionName) {
			return new TestAction(actionName);
		}

		@Override
		public void initialise(TestAction action) {
		}
	}
}
