package com.atomicleopard.webFramework.routes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.bind.ActionParameterBinder;
import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.logger.Logger;

public class ActionMethodResolver implements ActionResolver<ActionMethod> {

	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();

	private ActionParameterBinder binder;

	public ActionMethodResolver() {
		this.binder = new ActionParameterBinder();
	}

	public Object resolve(ActionMethod action, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		Object controller = getOrCreateController(action);
		List<?> arguments = binder.bind(action, req, resp, pathVars);
		Object result = action.invoke(controller, arguments);
		Logger.info("%s -> %s resolved", req.getRequestURI(), action);
		return result;
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
}
