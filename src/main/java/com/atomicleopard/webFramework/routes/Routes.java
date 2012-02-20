package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.mapKeys;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jodd.util.ClassLoaderUtil;
import jodd.util.ReflectUtil;
import scala.Tuple2;

import com.atomicleopard.webFramework.bind.ParameterBinderFinder;
import com.atomicleopard.webFramework.configuration.JsonProperties;
import com.atomicleopard.webFramework.exception.BaseException;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class Routes {
	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();
	private Map<String, ActionMethod> getRoutes = new LinkedHashMap<String, ActionMethod>();
	private Map<String, ActionMethod> postRoutes = new LinkedHashMap<String, ActionMethod>();
	private Map<String, ActionMethod> putRoutes = new LinkedHashMap<String, ActionMethod>();
	private Map<String, ActionMethod> deleteRoutes = new LinkedHashMap<String, ActionMethod>();
	private Map<String, ActionMethod> headRoutes = new LinkedHashMap<String, ActionMethod>();
	private Map<RouteType, Map<String, ActionMethod>> routes = mapKeys(RouteType.GET, RouteType.POST, RouteType.PUT, RouteType.DELETE, RouteType.HEAD).to(getRoutes, postRoutes, putRoutes,
			deleteRoutes, headRoutes);

	private ParameterBinderFinder binderFinder;

	public Routes() {
		this.binderFinder = new ParameterBinderFinder();
	}

	public void addRoutes(Map<String, Tuple2<String, RouteType>> routes) {
		for (Map.Entry<String, Tuple2<String, RouteType>> entry : routes.entrySet()) {
			String path = entry.getKey();
			String action = entry.getValue()._1();
			RouteType routeType = entry.getValue()._2();
			List<RouteType> routeTypesToAdd = routeType == null ? RouteType.all() : Collections.singletonList(routeType);
			ActionMethod actionMethod = findMethod(action);
			actionMethod.addParameterBinders(binderFinder);
			for (RouteType type : routeTypesToAdd) {
				this.routes.get(type).put(path, actionMethod);
			}
		}
	}

	public void invoke(String route) {
		ActionMethod invokableMethod = getRoutes.get(route);
		if (invokableMethod != null) {
			Object controller = getOrCreateController(invokableMethod);
			invokableMethod.invoke(controller);
		}
	}

	private Object getOrCreateController(ActionMethod actionMethod) {
		Object controller = controllerInstances.get(actionMethod.class1);
		if (controller == null) {
			synchronized (controllerInstances) {
				controller = controllerInstances.get(actionMethod.class1);
				if (controller == null) {
					try {
						controller = actionMethod.class1.newInstance();
					} catch (Exception e) {
						throw new BaseException(e, "Failed to create controller %s: %s", actionMethod.class1.toString(), e.getMessage());
					}
					controllerInstances.put(actionMethod.class1, controller);
				}
			}
		}
		return controller;
	}

	public ActionMethod findMethod(String route) {
		try {
			String methodName = StringUtil.lastToken(route, ".");
			String className = route.substring(0, route.length() - methodName.length() - 1);
			Class<?> clazz = ClassLoaderUtil.loadClass(className);
			Method method = ReflectUtil.findMethod(clazz, methodName);
			return new ActionMethod(clazz, method);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to find an action method for the route %s: %s", route, e.getMessage());
		}

	}

	public static Map<String, Tuple2<String, RouteType>> parseJsonRoutes(String source) throws ClassNotFoundException {
		Map<String, Tuple2<String, RouteType>> routeMap = new LinkedHashMap<String, Tuple2<String, RouteType>>();

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
			routeMap.put(route, new Tuple2<String, RouteType>(actionName, routeType));
		}

		return routeMap;
	}
}
