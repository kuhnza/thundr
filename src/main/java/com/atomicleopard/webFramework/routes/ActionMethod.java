package com.atomicleopard.webFramework.routes;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import com.atomicleopard.webFramework.exception.BaseException;

public class ActionMethod implements Action {
	public Class<?> class1;
	public Method method;
	public List<ActionParameter> parameters = new ArrayList<ActionParameter>();

	public ActionMethod(Class<?> class1, Method method) {
		super();
		this.class1 = class1;
		this.method = method;
		Type[] genericParameters = method.getGenericParameterTypes();
		MethodParameter[] parameterNames = Paramo.resolveParameters(method);
		for (int i = 0; i < genericParameters.length; i++) {
			String name = parameterNames[i].getName();
			this.parameters.add(new ActionParameter(name, genericParameters[i]));
		}
	}
	
	public Object invoke(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object invoke(Object controller, List<?> args) {
		try {
			return method.invoke(controller, args.toArray());
		} catch (Exception e) {
			throw new BaseException(e, "Failed to invoke controller method %s.%s: %s", class1.toString(), method.getName(), e.getMessage());
		}
	}
	
	@Override
	public String toString() {
		return String.format("ActionMethod %s", method.toString());
	}
}
