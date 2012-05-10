package com.threewks.thundr.action.method.bind.http;

import com.threewks.thundr.introspection.ParameterDescription;

public interface ParameterBinder<T> {
	public boolean willBind(ParameterDescription parameterDescription);
	public T bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap);
}
