package com.atomicleopard.webFramework.action.method.bind.http;

import com.atomicleopard.webFramework.introspection.ParameterDescription;

public interface ParameterBinder<T> {
	public boolean willBind(ParameterDescription parameterDescription);
	public T bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap);
}
