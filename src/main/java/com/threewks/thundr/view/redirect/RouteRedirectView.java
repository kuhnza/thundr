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

import java.util.Collections;
import java.util.Map;

import com.threewks.thundr.view.View;

public class RouteRedirectView implements View {
	private String route;
	private Map<String, Object> pathVariables;

	public RouteRedirectView(String route) {
		this(route, Collections.<String, Object> emptyMap());
	}

	public RouteRedirectView(String route, Map<String, Object> pathVariables) {
		this.route = route;
		this.pathVariables = pathVariables;
	}

	public String getRoute() {
		return route;
	}

	public Map<String, Object> getPathVariables() {
		return pathVariables;
	}

	@Override
	public String toString() {
		return String.format("Redirect to route '%s'", route);
	}

}
