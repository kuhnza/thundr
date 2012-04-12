package com.atomicleopard.webFramework.routes.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.atomicleopard.webFramework.routes.ActionException;
import com.atomicleopard.webFramework.routes.ActionResolver;
import com.atomicleopard.webFramework.routes.RouteType;

public class MethodActionResolver implements ActionResolver<MethodAction>, ActionInterceptorRegistry {

	private boolean enableCaching = true;
	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();
	private Map<Class<? extends Annotation>, ActionInterceptor<? extends Annotation>> actionInterceptors = new HashMap<Class<? extends Annotation>, ActionInterceptor<? extends Annotation>>();
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
			Class<?> clazz = Class.forName(className); // TODO - Restricted in GAE - why is this better? ClassLoaderUtil.loadClass(className);
			Method method = ReflectUtil.findMethod(clazz, methodName);
			if (method == null) {
				return null;
			}
			return new MethodAction(clazz, method, findInterceptors(method));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Object resolve(MethodAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		Object controller = getOrCreateController(action);
		List<?> arguments = binder.bind(action, req, resp, pathVars);
		Map<Annotation, ActionInterceptor<Annotation>> interceptors = action.interceptors();
		Object result = null;
		try {
			result = beforeInterceptors(interceptors, req, resp);
			result = result != null ? result : action.invoke(controller, arguments);
			result = afterInterceptors(result, interceptors, req, resp);
		} catch (Exception e) {
			result = exceptionInterceptors(interceptors, req, resp, e);
			if (result == null) {
				throw new ActionException(e, "Failed to invoke controller %s: %s", action, e.getMessage());
			}
		}
		Logger.info("%s -> %s resolved", req.getRequestURI(), action);
		return result;
	}

	private Object afterInterceptors(Object result, Map<Annotation, ActionInterceptor<Annotation>> interceptors, HttpServletRequest req, HttpServletResponse resp) {
		for (Map.Entry<Annotation, ActionInterceptor<Annotation>> interceptorEntry : interceptors.entrySet()) {
			Object interceptorResult = interceptorEntry.getValue().after(interceptorEntry.getKey(), req, resp);
			if (interceptorResult != null) {
				return interceptorResult;
			}
		}

		return result;
	}

	private Object exceptionInterceptors(Map<Annotation, ActionInterceptor<Annotation>> interceptors, HttpServletRequest req, HttpServletResponse resp, Exception e) {
		for (Map.Entry<Annotation, ActionInterceptor<Annotation>> interceptorEntry : interceptors.entrySet()) {
			Object interceptorResult = interceptorEntry.getValue().exception(interceptorEntry.getKey(), e, req, resp);
			if (interceptorResult != null) {
				return interceptorResult;
			}
		}
		return null;
	}

	private Object beforeInterceptors(Map<Annotation, ActionInterceptor<Annotation>> interceptors, HttpServletRequest req, HttpServletResponse resp) {
		for (Map.Entry<Annotation, ActionInterceptor<Annotation>> interceptorEntry : interceptors.entrySet()) {
			Object interceptorResult = interceptorEntry.getValue().before(interceptorEntry.getKey(), req, resp);
			if (interceptorResult != null) {
				return interceptorResult;
			}
		}
		return null;
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

	@SuppressWarnings("unchecked")
	private Map<Annotation, ActionInterceptor<Annotation>> findInterceptors(Method method) {
		Map<Annotation, ActionInterceptor<Annotation>> interceptors = new LinkedHashMap<Annotation, ActionInterceptor<Annotation>>();
		for (Annotation annotation : method.getDeclaredAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			ActionInterceptor<Annotation> actionInterceptor = (ActionInterceptor<Annotation>) actionInterceptors.get(annotationType);
			if (actionInterceptor != null) {
				interceptors.put(annotation, actionInterceptor);
			}
		}

		return interceptors;
	}

	@Override
	public <A extends Annotation> void registerInterceptor(Class<A> annotation, ActionInterceptor<A> interceptor) {
		actionInterceptors.put(annotation, interceptor);
	}
}
