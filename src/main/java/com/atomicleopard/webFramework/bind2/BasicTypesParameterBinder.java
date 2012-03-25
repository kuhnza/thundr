package com.atomicleopard.webFramework.bind2;

import static com.atomicleopard.expressive.Expressive.list;
import jodd.typeconverter.TypeConverterManager;

import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class BasicTypesParameterBinder implements ParameterBinder<Object> {
	public Object bind(Binders binder, ParameterDescription parameterDescription, PathMap pathMap) {
		String[] values = pathMap.get(list(parameterDescription.name()));
		return values != null && values.length > 0 ? TypeConverterManager.lookup(parameterDescription.type()).convert(values[0]) : null;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return TypeConverterManager.lookup(parameterDescription.type()) != null;
	}
}
