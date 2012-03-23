package com.atomicleopard.webFramework.bind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.atomicleopard.webFramework.bind2.InstanceParameterBinder;
import com.atomicleopard.webFramework.bind2.PathMap;
import com.atomicleopard.webFramework.introspection.ParameterDescription;
import com.atomicleopard.webFramework.routes.ActionMethod;

public class ActionParameterBinder {
	public List<Object> bind(ActionMethod actionMethod, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		List<ParameterDescription> parameterDescriptions = actionMethod.parameters();
		PathMap pathMap = createPathMap(req, pathVars);
		Binders binders = binders(req, resp, pathVars);
		return binders.createFor(parameterDescriptions, pathMap);
	}

	private PathMap createPathMap(HttpServletRequest req, Map<String, String> pathVars) {
		Map<String, String[]> requestParameters = new HashMap<String, String[]>(req.getParameterMap());
		if (pathVars != null) {
			for (Map.Entry<String, String> entry : pathVars.entrySet()) {
				requestParameters.put(entry.getKey(), new String[] { entry.getValue() });
			}
		}
		PathMap pathMap = new PathMap(requestParameters);
		return pathMap;
	}

	private Binders binders(HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		Binders binders = new Binders();
		InstanceParameterBinder requestBinder = new InstanceParameterBinder(req);
		InstanceParameterBinder responseBinder = new InstanceParameterBinder(resp);
		InstanceParameterBinder sessionBinder = new InstanceParameterBinder(req == null ? null : req.getSession());

		binders.addBinder(HttpSession.class, sessionBinder);
		binders.addBinder(HttpServletRequest.class, requestBinder);
		binders.addBinder(HttpServletResponse.class, responseBinder);
		binders.addBinder(ServletRequest.class, requestBinder);
		binders.addBinder(ServletResponse.class, responseBinder);
		binders.addDefaultBinders();
		return binders;
	}
}
