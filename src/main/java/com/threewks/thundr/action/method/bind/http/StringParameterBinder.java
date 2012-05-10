package com.threewks.thundr.action.method.bind.http;

import static com.atomicleopard.expressive.Expressive.list;

import com.threewks.thundr.introspection.ParameterDescription;

public class StringParameterBinder implements ParameterBinder<String> {
	public String bind(ParameterBinderSet binder, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] values = pathMap.get(list(parameterDescription.name()));
		return values != null && values.length > 0 ? values[0] : null;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.isA(String.class);
	}
}
