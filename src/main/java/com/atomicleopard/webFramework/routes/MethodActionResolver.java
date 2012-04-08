package com.atomicleopard.webFramework.routes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.ReflectUtil;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.bind.ActionParameterBinder;
import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;

public class MethodActionResolver implements ActionResolver<MethodAction> {

	private boolean enableCaching = true;
	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();
	private ActionParameterBinder binder = new ActionParameterBinder();
	private UpdatableInjectionContext injectionContext;

	public MethodActionResolver(UpdatableInjectionContext injectionContext) {
		this.injectionContext = injectionContext;
	}

	@Override
	public MethodAction createActionIfPossible(String actionName) {
		// will resolve if both a class and method name can be parsed, and a valid class with that method name can be loaded
		String methodName = MethodAction.methodNameForAction(actionName);
		String className = MethodAction.classNameForAction(actionName);
		if (StringUtils.isEmpty(methodName) || StringUtils.isEmpty(className)) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(className, false, this.getClass().getClassLoader());
			Method method = ReflectUtil.findMethod(clazz, methodName);
			if (method == null) {
				return null;
			}
			return new MethodAction(actionName);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Object resolve(MethodAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		Object controller = getOrCreateController(action);
		List<?> arguments = binder.bind(action, req, resp, pathVars);
		Object result = action.invoke(controller, arguments);
		Logger.info("%s -> %s resolved", req.getRequestURI(), action);
		return result;
	}

	private Object getOrCreateController(MethodAction actionMethod) {
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

	private <T> T createController(MethodAction actionMethod) {
		Class<T> type = actionMethod.type();
		if (!injectionContext.contains(type)) {
			injectionContext.inject(type).as(type);
		}
		try {
			return injectionContext.get(type);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to create controller %s: %s", type.toString(), e.getMessage());
		}
	}
}
