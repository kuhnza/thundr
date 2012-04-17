package com.atomicleopard.webFramework.route;

import java.util.List;

import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.util.Streams;

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
		Logger.debug("Loaded routes");
	}
}
