package com.atomicleopard.webFramework.bind2;

import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class InstanceParameterBinder implements ParameterBinder<Object> {
	private Object instance;

	public InstanceParameterBinder(Object instance) {
		this.instance = instance;
	}

	public Object bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		return parameterDescription.isA(instance.getClass()) ? instance : null;
	}
}
