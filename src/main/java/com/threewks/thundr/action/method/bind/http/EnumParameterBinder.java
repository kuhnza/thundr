package com.threewks.thundr.action.method.bind.http;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.introspection.ParameterDescription;

public class EnumParameterBinder implements ParameterBinder<Object> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] stringValues = pathMap.get(parameterDescription.name());
		String stringValue = stringValues == null || stringValues.length == 0 ? null : stringValues[0];
		Class<?> classType = parameterDescription.classType();
		Class<Enum> enumType = (Class<Enum>) classType;
		return Expressive.Transformers.toEnum(enumType).from(stringValue);
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.classType().isEnum();
	}

}
