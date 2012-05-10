package com.threewks.thundr.action.method.bind.http;

import java.util.List;

import com.threewks.thundr.introspection.ParameterDescription;

public class NamedInstanceParameterBinder implements ParameterBinder<Object> {
	private InstanceParameterBinder delegate;
	private List<String> names;

	public NamedInstanceParameterBinder(List<String> names, Object instance) {
		this.names = names;
		this.delegate = new InstanceParameterBinder(instance);
	}

	public Object bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		return delegate.bind(binders, parameterDescription, pathMap);
	}
	
	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return names.contains(parameterDescription.name()) && delegate.willBind(parameterDescription); 
	}
}
