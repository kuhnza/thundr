package com.atomicleopard.webFramework.bind2;

import java.util.List;

import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class NamedInstanceParameterBinder implements ParameterBinder<Object> {
	private InstanceParameterBinder delegate;
	private List<String> names;

	public NamedInstanceParameterBinder(List<String> names, Object instance) {
		this.names = names;
		this.delegate = new InstanceParameterBinder(instance);
	}

	public Object bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		return names.contains(parameterDescription.name()) ? delegate.bind(binders, parameterDescription, pathMap) : null;
	}
}
