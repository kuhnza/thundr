package com.atomicleopard.webFramework.routes;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionResolver<T extends Action> {
	public Object resolve(T action, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars);
}
