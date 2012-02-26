package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.ClassLoaderUtil;
import jodd.util.KeyValue;
import jodd.util.ReflectUtil;
import jodd.util.Wildcard;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.configuration.JsonProperties;
import com.atomicleopard.webFramework.exception.BaseException;

public class Routes {
	private static final String Static = "static";
	private Map<String, Action> getRoutes = new LinkedHashMap<String, Action>();
	private Map<String, Action> postRoutes = new LinkedHashMap<String, Action>();
	private Map<String, Action> putRoutes = new LinkedHashMap<String, Action>();
	private Map<String, Action> deleteRoutes = new LinkedHashMap<String, Action>();
	private Map<RouteType, Map<String, Action>> routes = mapKeys(RouteType.GET, RouteType.POST, RouteType.PUT, RouteType.DELETE).to(getRoutes, postRoutes, putRoutes, deleteRoutes);

	private Map<Class<? extends Action>, ActionResolver<?>> actionResolvers = new LinkedHashMap<Class<? extends Action>, ActionResolver<?>>();

	private boolean debug = true;

	public Routes() {
		actionResolvers.put(StaticResourceAction.class, new StaticResourceActionResolver());
		actionResolvers.put(ActionMethod.class, new ActionMethodResolver());
	}

	public void addRoutes(Map<String, KeyValue<String, RouteType>> routes) {
		for (Map.Entry<String, KeyValue<String, RouteType>> entry : routes.entrySet()) {
			String path = entry.getKey();
			String actionName = entry.getValue().getKey();
			RouteType routeType = entry.getValue().getValue();
			List<RouteType> routeTypesToAdd = routeType == null ? RouteType.all() : Collections.singletonList(routeType);
			Action action = findMethod(actionName);
			for (RouteType type : routeTypesToAdd) {
				this.routes.get(type).put(path, action);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Action> Object invoke(String route, RouteType routeType, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Action> routesForRouteType = routes.get(routeType);
		String[] availableRoutes = routesForRouteType.keySet().toArray(new String[0]);
		int match = Wildcard.matchOne(route, availableRoutes);
		if (match > -1) {
			String matchingRoute = availableRoutes[match];
			T action = (T) routesForRouteType.get(matchingRoute);
			ActionResolver<T> actionResolver = (ActionResolver<T>) actionResolvers.get(action.getClass());
			return actionResolver.resolve(action, req, resp);
		}
		String debugString = debug ? listRoutes() : "";
		throw new RouteException("No route matching the request %s %s%s", routeType, route, debugString);
	}

	private static final String routeDisplayFormat = "%s\t%s ->\t%s\n";

	@SuppressWarnings("unchecked")
	private String listRoutes() {
		List<String> allRoutes = flatten(getRoutes.keySet(), postRoutes.keySet(), putRoutes.keySet(), deleteRoutes.keySet());
		allRoutes = list(new HashSet<String>(allRoutes));
		
		Collections.sort(allRoutes);
		StringBuilder sb = new StringBuilder();
		for (String route : allRoutes) {
			if (getRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, "GET", route, getRoutes.get(route)));
			}
			if (postRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, "POST", route, postRoutes.get(route)));
			}
			if (putRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, "PUT", route, putRoutes.get(route)));
			}
			if (deleteRoutes.containsKey(route)) {
				sb.append(String.format(routeDisplayFormat, "DELETE", route, deleteRoutes.get(route)));
			}
		}
		return sb.toString();
	}

	public Action findMethod(String actionName) {
		if (Static.equalsIgnoreCase(actionName)) {
			return new StaticResourceAction();
		}
		try {
			String methodName = StringUtils.substringAfterLast(actionName, ".");
			String className = StringUtils.substringBeforeLast(actionName, ".");
			Class<?> clazz = ClassLoaderUtil.loadClass(className);
			Method method = ReflectUtil.findMethod(clazz, methodName);
			return new ActionMethod(clazz, method);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to find an action method for the route %s: %s", actionName, e.getMessage());
		}

	}

	public static Map<String, KeyValue<String, RouteType>> parseJsonRoutes(String source) throws ClassNotFoundException {
		Map<String, KeyValue<String, RouteType>> routeMap = new LinkedHashMap<String, KeyValue<String, RouteType>>();

		JsonProperties properties = new JsonProperties(source);
		for (String route : properties.getKeys()) {
			String actionName = null;
			RouteType routeType = null;
			if (properties.is(route, String.class)) {
				// simple route
				actionName = properties.getString(route);
			} else if (properties.is(route, List.class)) {
				// complex route
				List<String> list = properties.getList(route);
				actionName = list.get(0);
				routeType = RouteType.from(list.get(1));
			}
			routeMap.put(route, new KeyValue<String, RouteType>(actionName, routeType));
		}

		return routeMap;
	}
}
