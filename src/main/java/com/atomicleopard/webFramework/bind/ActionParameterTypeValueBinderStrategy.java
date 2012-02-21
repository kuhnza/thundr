package com.atomicleopard.webFramework.bind;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class ActionParameterTypeValueBinderStrategy<T> implements ActionParameterBinderStrategy {

	private Class<T> clazz;
	private T value;

	public ActionParameterTypeValueBinderStrategy(Class<T> clazz, T value) {
		this.value = value;
		this.clazz = clazz;
	}

	public void bind(ActionParameter actionParameter, Map<String, Object> bindings) {
		if (actionParameter.isA(clazz)) {
			bindings.put(actionParameter.name, value);
		}
	}
}
