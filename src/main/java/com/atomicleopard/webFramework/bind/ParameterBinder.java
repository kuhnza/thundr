package com.atomicleopard.webFramework.bind;

import com.atomicleopard.webFramework.bind.http.PathMap;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public interface ParameterBinder<T> {
	public boolean willBind(ParameterDescription parameterDescription);
	public T bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap);
}
