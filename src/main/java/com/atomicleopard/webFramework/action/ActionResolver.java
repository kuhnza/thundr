package com.atomicleopard.webFramework.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.route.RouteType;

public interface ActionResolver<T extends Action> {
	public Object resolve(T action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException;

	public T createActionIfPossible(String actionName);
}
