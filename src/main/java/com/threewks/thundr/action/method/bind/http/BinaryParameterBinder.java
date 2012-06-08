package com.threewks.thundr.action.method.bind.http;

import com.threewks.thundr.introspection.ParameterDescription;

public interface BinaryParameterBinder<T> {
	public boolean willBind(ParameterDescription parameterDescription);
	public T bind(ParameterDescription parameterDescription, byte[] data);
}
