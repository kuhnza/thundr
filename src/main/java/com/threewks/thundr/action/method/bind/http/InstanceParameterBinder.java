package com.threewks.thundr.action.method.bind.http;

import com.threewks.thundr.introspection.ParameterDescription;

public class InstanceParameterBinder implements ParameterBinder<Object> {
	private Object instance;

	public InstanceParameterBinder(Object instance) {
		this.instance = instance;
	}

	public Object bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		return instance;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return instance == null ? false : parameterDescription.isA(instance.getClass());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " for " + instance.getClass().getSimpleName() + " - " + instance.toString();
	}
}
