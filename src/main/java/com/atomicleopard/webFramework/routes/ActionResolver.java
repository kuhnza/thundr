package com.atomicleopard.webFramework.routes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionResolver<T extends Action> {
	public Object resolve(T action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars);

	public T createActionIfPossible(String actionName);
}
