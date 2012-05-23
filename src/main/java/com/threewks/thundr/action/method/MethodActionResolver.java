package com.threewks.thundr.action.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.ReflectUtil;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.Cast;
import com.threewks.thundr.action.ActionException;
import com.threewks.thundr.action.ActionResolver;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.action.method.bind.json.GsonBinder;
import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.exception.BaseException;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.route.RouteType;

public class MethodActionResolver implements ActionResolver<MethodAction>, ActionInterceptorRegistry {

	private Map<Class<?>, Object> controllerInstances = new HashMap<Class<?>, Object>();
	private Map<Class<? extends Annotation>, ActionInterceptor<? extends Annotation>> actionInterceptors = new HashMap<Class<? extends Annotation>, ActionInterceptor<? extends Annotation>>();
	private List<ActionMethodBinder> methodBinders;
	private UpdatableInjectionContext injectionContext;

	public MethodActionResolver(UpdatableInjectionContext injectionContext) {
		this.injectionContext = injectionContext;
		PathVariableBinder pathVariableBinder = new PathVariableBinder();
		methodBinders = new ArrayList<ActionMethodBinder>();
		methodBinders.add(new GsonBinder(pathVariableBinder));
		methodBinders.add(new HttpBinder(pathVariableBinder));
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
			MethodAction methodAction = new MethodAction(clazz, method, findInterceptors(method));
			// force instantiation of controller - this allows controllers to be injected into eachother
			// and also flushes out instantiation issues at startup
			Object controller = createController(methodAction);
			controllerInstances.put(methodAction.type(), controller);
			return methodAction;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Object resolve(MethodAction action, RouteType routeType, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) throws ActionException {
		Object controller = getOrCreateController(action);
		List<?> arguments = bindArguments(action, req, resp, pathVars);
		Map<Annotation, ActionInterceptor<Annotation>> interceptors = action.interceptors();
		Object result = null;
		Exception exception = null;
		try {
			result = beforeInterceptors(interceptors, req, resp);
			result = result != null ? result : action.invoke(controller, arguments);
			result = afterInterceptors(result, interceptors, req, resp);
		} catch (InvocationTargetException e) {
			// we need to unwrap InvocationTargetExceptions to get at the real exception
			exception = Cast.as(e.getTargetException(), Exception.class);
			if (exception == null) {
				throw new BaseException(e);
			}
		} catch (Exception e) {
			exception = e;
		}
		if (exception != null) {
			result = exceptionInterceptors(interceptors, req, resp, exception);
			if (result == null) {
				throw new ActionException(exception, "Failed in %s: %s", action, exception.getMessage());
			}
		}
		Logger.debug("%s -> %s resolved", req.getRequestURI(), action);
		return result;
	}

	private List<Object> bindArguments(MethodAction action, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVars) {
		List<ParameterDescription> parameterDescriptions = action.parameters();
		String sanitisedContentType = ContentType.cleanContentType(req.getContentType());
		for (ActionMethodBinder binder : methodBinders) {
			if (binder.canBind(sanitisedContentType)) {
				return binder.bindAll(parameterDescriptions, req, resp, pathVars);
			}
		}
		throw new BindException("Cannot bind arguments - no binder found for Content-Type=%s", req.getContentType());
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

	private Object getOrCreateController(MethodAction methodAction) {
		Object controller = controllerInstances.get(methodAction.type());
		if (controller == null) {
			synchronized (controllerInstances) {
				controller = controllerInstances.get(methodAction.type());
				if (controller == null) {
					controller = createController(methodAction);
					controllerInstances.put(methodAction.type(), controller);
				}
			}
		}
		return controller;
	}

	<T> T createController(MethodAction actionMethod) {
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

	Map<Annotation, ActionInterceptor<Annotation>> findInterceptors(Method method) {
		Map<Annotation, ActionInterceptor<Annotation>> interceptors = new LinkedHashMap<Annotation, ActionInterceptor<Annotation>>();
		for (Annotation annotation : method.getDeclaredAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			ActionInterceptor<Annotation> actionInterceptor = interceptor(annotationType);
			if (actionInterceptor != null) {
				interceptors.put(annotation, actionInterceptor);
			}
		}

		return interceptors;
	}

	@Override
	public <A extends Annotation> void registerInterceptor(Class<A> annotation, ActionInterceptor<A> interceptor) {
		actionInterceptors.put(annotation, interceptor);
		Logger.info("Added ActionInterceptor %s for methods annotated with %s", interceptor, annotation);
	}

	@SuppressWarnings("unchecked")
	public ActionInterceptor<Annotation> interceptor(Class<? extends Annotation> annotationType) {
		return (ActionInterceptor<Annotation>) actionInterceptors.get(annotationType);
	}
}
