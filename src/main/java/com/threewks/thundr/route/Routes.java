package com.threewks.thundr.route;

import static com.atomicleopard.expressive.Expressive.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.Action;
import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;
import com.threewks.thundr.configuration.JsonProperties;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.profiler.Profilable;
import com.threewks.thundr.profiler.Profiler;

public class Routes {

	private Map<String, Route> getRoutes = new LinkedHashMap<String, Route>();
	private Map<String, Route> postRoutes = new LinkedHashMap<String, Route>();
	private Map<String, Route> putRoutes = new LinkedHashMap<String, Route>();
	private Map<String, Route> deleteRoutes = new LinkedHashMap<String, Route>();
	private Map<Route, Action> actionsForRoutes = new HashMap<Route, Action>();
	@SuppressWarnings("unchecked")
	private Map<RouteType, Map<String, Route>> routes = mapKeys(RouteType.GET, RouteType.POST, RouteType.PUT, RouteType.DELETE).to(getRoutes, postRoutes, putRoutes, deleteRoutes);

	private Map<Class<? extends Action>, ActionResolver<?>> actionResolvers = new LinkedHashMap<Class<? extends Action>, ActionResolver<?>>();

	private boolean debug = true;

	public void addRoutes(Collection<Route> routes) {
		for (Route route : routes) {
			String path = route.getRouteMatchRegex();
			String actionName = route.getActionName();
			RouteType routeType = route.getRouteType();
			Action action = createAction(actionName);
			this.routes.get(routeType).put(path, route);
			this.actionsForRoutes.put(route, action);
			Logger.info("Added route %s", route);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Action> Object invoke(String routePath, RouteType routeType, HttpServletRequest req, HttpServletResponse resp) {
		Logger.debug("Requesting '%s'", routePath);
		Map<String, Route> routesForRouteType = routes.get(routeType);
		for (Route route : routesForRouteType.values()) {
			if (route.matches(routePath)) {
				T action = (T) actionsForRoutes.get(route);
				return resolveAction(routePath, routeType, req, resp, route, action);
			}
		}
		String debugString = debug ? listRoutes() : "";
		throw new RouteNotFoundException("No route matching the request %s %s\n%s", routeType, routePath, debugString);
	}

	@SuppressWarnings("unchecked")
	private <T extends Action> Object resolveAction(final String routePath, final RouteType routeType, final HttpServletRequest req, final HttpServletResponse resp, final Route route, final T action) {
		Profiler profiler = getProfiler(req);
		return profiler.profile(Profiler.CategoryAction, action.toString(), new Profilable<Object>() {
			@Override
			public Object profile() {
				Map<String, String> pathVars = route.getPathVars(routePath);
				ActionResolver<T> actionResolver = (ActionResolver<T>) actionResolvers.get(action.getClass());
				Object resolve = actionResolver.resolve(action, routeType, req, resp, pathVars);
				return resolve;
			}
		});
	}

	private Profiler getProfiler(HttpServletRequest req) {
		return (Profiler) req.getAttribute("com.threewks.thundr.profiler.Profiler");
	}

	private static final String routeDisplayFormat = "%s\n";

	@SuppressWarnings("unchecked")
	private String listRoutes() {
		List<String> allRoutes = flatten(getRoutes.keySet(), postRoutes.keySet(), putRoutes.keySet(), deleteRoutes.keySet());
		allRoutes = list(new HashSet<String>(allRoutes));

		Collections.sort(allRoutes);
		StringBuilder sb = new StringBuilder();
		for (String route : allRoutes) {
			if (getRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, getRoutes.get(route)));
			}
			if (postRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, postRoutes.get(route)));
			}
			if (putRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, putRoutes.get(route)));
			}
			if (deleteRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, deleteRoutes.get(route)));
			}
		}
		return sb.toString();
	}

	public Action createAction(String actionName) {
		try {
			for (ActionResolver<?> actionResolver : actionResolvers.values()) {
				Action action = actionResolver.createActionIfPossible(actionName);
				if (action != null) {
					return action;
				}
			}
			throw new ActionException("Failed to create an action for the route %s: No action resolver can resolve this action", actionName);
		} catch (ActionException e) {
			throw e;
		} catch (Exception e) {
			throw new ActionException(e, "Failed to create an action for the route %s: %s", actionName, e.getMessage());
		}

	}

	// TODO - This method should probably be externalised from this class, routes should theoretically be buildable however someone wants
	public static List<Route> parseJsonRoutes(String source) {
		try {
			List<Route> routes = new ArrayList<Route>();
			JsonProperties properties = new JsonProperties(source);
			for (String route : properties.getKeys()) {
				if (properties.is(route, String.class)) {
					// simple route
					String actionName = properties.getString(route);
					routes.add(new Route(route, actionName, RouteType.GET));
				} else if (properties.is(route, Map.class)) {
					// complex route
					Map<String, String> map = properties.getMap(route);
					for (Map.Entry<String, String> routeEntry : map.entrySet()) {
						RouteType routeType = RouteType.from(routeEntry.getKey());
						if (routeType == null) {
							throw new RouteNotFoundException("Unknown route type %s", routeEntry.getKey());
						}
						routes.add(new Route(route, routeEntry.getValue(), routeType));
					}
				}
			}

			return routes;
		} catch (Exception e) {
			throw new RouteNotFoundException(e, "Failed to parse routes: %s", e.getMessage());
		}
	}

	public <A extends Action> void addActionResolver(Class<A> actionType, ActionResolver<A> actionResolver) {
		actionResolvers.put(actionType, actionResolver);
		Logger.debug("Added action resolver %s for actions of type %s", actionResolver, actionType);
	}
}
