package com.atomicleopard.webFramework.routes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.atomicleopard.expressive.Cast;

public class ActionParameter {
	public String name;
	public Type type;

	public ActionParameter(String name, Type type) {
		super();
		this.name = name;
		this.type = type;
	}

	public boolean isA(Class<?> is) {
		return is.isAssignableFrom(type.getClass());
	}

	public Type getGenericType(int i) {
		ParameterizedType pType = Cast.as(type, ParameterizedType.class);
		if (pType != null) {
			return pType.getActualTypeArguments()[i];
		}
		return null;
	}
}
