package com.atomicleopard.webFramework.routes.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.introspection.ParameterDescription;
import com.atomicleopard.webFramework.routes.Action;

public class MethodAction implements Action {
	private Class<?> class1;
	private Method method;
	private Map<Annotation, ActionInterceptor<Annotation>> interceptors;

	private List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

	public MethodAction(Class<?> class1, Method method, Map<Annotation, ActionInterceptor<Annotation>> interceptors) {
		this.class1 = class1;
		this.method = method;
		this.interceptors = interceptors;
		Type[] genericParameters = method.getGenericParameterTypes();
		MethodParameter[] parameterNames = Paramo.resolveParameters(method);
		for (int i = 0; i < genericParameters.length; i++) {
			String name = parameterNames[i].getName();
			this.parameters.add(new ParameterDescription(name, genericParameters[i]));
		}
	}

	public List<ParameterDescription> parameters() {
		return parameters;
	}

	public Object invoke(Object controller, List<?> args) throws Exception {
		return method.invoke(controller, args.toArray());
	}

	@Override
	public String toString() {
		return method.toString();
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> type() {
		return (Class<T>) class1;
	}

	public Method method() {
		return method;
	}

	public Map<Annotation, ActionInterceptor<Annotation>> interceptors() {
		return interceptors;
	}

	static final String classNameForAction(String actionName) {
		return StringUtils.substringBeforeLast(actionName, ".");
	}

	static final String methodNameForAction(String actionName) {
		return StringUtils.substringAfterLast(actionName, ".");
	}
}
