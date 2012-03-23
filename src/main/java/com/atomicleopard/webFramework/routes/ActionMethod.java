package com.atomicleopard.webFramework.routes;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;
import jodd.util.ClassLoaderUtil;
import jodd.util.ReflectUtil;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class ActionMethod implements Action {
	private Class<?> class1;
	private Method method;
	private List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

	public ActionMethod(String actionName) throws ClassNotFoundException {
		String methodName = StringUtils.substringAfterLast(actionName, ".");
		String className = StringUtils.substringBeforeLast(actionName, ".");
		this.class1 = ClassLoaderUtil.loadClass(className);
		this.method = ReflectUtil.findMethod(class1, methodName);
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

	public Object invoke(Object controller, List<?> args) {
		try {
			return method.invoke(controller, args.toArray());
		} catch (Exception e) {
			Throwable original = e.getCause() == null ? e : e.getCause();
			throw new BaseException(original, "Failed to invoke controller method %s.%s: %s", class1.toString(), method.getName(), original.getMessage());
		}
	}

	@Override
	public String toString() {
		return method.toString();
	}

	public Class<?> type() {
		return class1;
	}
}
