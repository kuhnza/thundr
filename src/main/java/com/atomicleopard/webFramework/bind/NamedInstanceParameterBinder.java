package com.atomicleopard.webFramework.bind;

import java.util.List;

import com.atomicleopard.webFramework.bind.http.PathMap;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class NamedInstanceParameterBinder implements ParameterBinder<Object> {
	private InstanceParameterBinder delegate;
	private List<String> names;

	public NamedInstanceParameterBinder(List<String> names, Object instance) {
		this.names = names;
		this.delegate = new InstanceParameterBinder(instance);
	}

	public Object bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		return delegate.bind(binders, parameterDescription, pathMap);
	}
	
	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return names.contains(parameterDescription.name()) && delegate.willBind(parameterDescription); 
	}
}
