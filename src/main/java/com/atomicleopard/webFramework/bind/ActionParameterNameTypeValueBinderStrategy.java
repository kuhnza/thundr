package com.atomicleopard.webFramework.bind;

import java.util.Map;


public class ActionParameterNameTypeValueBinderStrategy<T> implements ActionParameterBinderStrategy {
	private String name;
	private ActionParameterTypeValueBinderStrategy<T> delegate;

	public ActionParameterNameTypeValueBinderStrategy(Class<T> clazz, String name, T value) {
		this.name = name;
		delegate = new ActionParameterTypeValueBinderStrategy<T>(clazz, value);
	}

	public void bind(ActionParameter actionParameter, Map<String, Object> bindings) {
		if (actionParameter.name.equals(this.name)) {
			delegate.bind(actionParameter, bindings);
		}
	}
}
