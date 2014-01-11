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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.route.ReverseRouteException;
import com.threewks.thundr.route.Route;
import com.threewks.thundr.route.Routes;
import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class RouteRedirectViewResolver implements ViewResolver<RouteRedirectView> {
	private Routes routes;

	public RouteRedirectViewResolver(Routes routes) {
		this.routes = routes;
	}

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, RouteRedirectView viewResult) {
		String routeName = viewResult.getRoute();
		Route route = this.routes.getRoute(routeName);
		if (route == null) {
			throw new ViewResolutionException("Cannot redirect to the route named '%s': no route with this name exists", routeName);
		}
		String reverseRoute = getReverseRoute(viewResult, route);
		try {
			resp.sendRedirect(reverseRoute);
		} catch (IOException e) {
			throw new ViewResolutionException(e, "Failed to redirect to route '%s' (%s): %s", routeName, reverseRoute, e.getMessage());
		}
	}

	private String getReverseRoute(RouteRedirectView viewResult, Route route) {
		try {
			return route.getReverseRoute(viewResult.getPathVariables());
		} catch (ReverseRouteException e) {
			throw new ViewResolutionException("Failed to redirect to route '%s': %s", route.getRoute(), e.getMessage());
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
