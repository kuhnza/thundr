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

import java.util.List;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.util.Streams;

public class RouteInjectionConfiguration implements InjectionConfiguration {
	private String filename;

	public RouteInjectionConfiguration() {
		this("routes.json");
	}

	public RouteInjectionConfiguration(String filename) {
		this.filename = filename;
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Routes routes = injectionContext.get(Routes.class);
		addRoutes(routes, injectionContext, filename);
	}

	protected void addRoutes(Routes routes, UpdatableInjectionContext injectionContext, String routesFile) {
		Logger.info("Loading routes from %s", routesFile);
		String routesSource = Streams.getResourceAsString(routesFile);
		List<Route> routeMap = Routes.parseJsonRoutes(routesSource);
		routes.addRoutes(routeMap);
		if (Logger.willDebug()) {
			Logger.debug("Loaded routes: \n%s", routes.listRoutes());
		}
	}
}
