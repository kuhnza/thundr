package com.atomicleopard.webFramework.routes;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import com.atomicleopard.webFramework.bind.ActionParameterBinder;
import com.atomicleopard.webFramework.bind.ParameterBinderFinder;
import com.atomicleopard.webFramework.exception.BaseException;

public class ActionMethod {
	public Class<?> class1;
	public Method method;
	public Map<String, ActionParameter> parameters = new LinkedHashMap<String, ActionParameter>();

	public ActionMethod(Class<?> class1, Method method) {
		super();
		this.class1 = class1;
		this.method = method;
		Type[] genericParameters = method.getGenericParameterTypes();
		MethodParameter[] parameterNames = Paramo.resolveParameters(method);
		for (int i = 0; i < genericParameters.length; i++) {
			String name = parameterNames[i].getName();
			this.parameters.put(name, new ActionParameter(name, genericParameters[i]));
		}
	}

	public void invoke(Object controller, Object... args) {
		try {
			method.invoke(controller, args);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to invoke controller method %s.%s: %s", class1.toString(), method.getName(), e.getMessage());
		}
	}
	
	public void addParameterBinders(ParameterBinderFinder binderFinder) {
		for (ActionParameter parameter : parameters.values()) {
			ActionParameterBinder<?> binder = binderFinder.findBinder(parameter);
			parameter.binder = binder;
		}
	}
}
