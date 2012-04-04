package com.atomicleopard.webFramework.routes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.bind.ActionParameterBinder;
import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;

public class ActionMethodResolver implements ActionResolver<ActionMethod> {

	private boolean enableCaching = true;
	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();
	private ActionParameterBinder binder = new ActionParameterBinder();
	private UpdatableInjectionContext injectionContext;

	public ActionMethodResolver(UpdatableInjectionContext injectionContext) {
		this.injectionContext = injectionContext;
	}

	public Object resolve(ActionMethod action, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		Object controller = getOrCreateController(action);
		List<?> arguments = binder.bind(action, req, resp, pathVars);
		Object result = action.invoke(controller, arguments);
		Logger.info("%s -> %s resolved", req.getRequestURI(), action);
		return result;
	}

	private Object getOrCreateController(ActionMethod actionMethod) {
		if (enableCaching) {
			Object controller = controllerInstances.get(actionMethod.type());
			if (controller == null) {
				synchronized (controllerInstances) {
					controller = controllerInstances.get(actionMethod.type());
					if (controller == null) {
						controller = createController(actionMethod);
						controllerInstances.put(actionMethod.type(), controller);
					}
				}
			}
			return controller;
		} else {
			return createController(actionMethod);
		}
	}

	private Object createController(ActionMethod actionMethod) {
		try {
			return injectionContext.get(actionMethod.type());
		} catch (Exception e) {
			throw new BaseException(e, "Failed to create controller %s: %s", actionMethod.type().toString(), e.getMessage());
		}
	}
}
