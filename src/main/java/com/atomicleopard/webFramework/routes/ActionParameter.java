package com.atomicleopard.webFramework.routes;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import jodd.util.ReflectUtil;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.webFramework.logger.Logger;

public class ActionParameter {
	public String name;
	public Type type;

	public ActionParameter(String name, Type type) {
		super();
		this.name = name;
		this.type = type;
	}

	public boolean isA(Class<?> is) {
		Class<?> clazz = ReflectUtil.toClass(type);
		if(clazz == null){
			Logger.warn("Clazz is null, type is %s", type);
		}
		
		return clazz == null ? false : is.isAssignableFrom(clazz);
	}

	public Type getGenericType(int i) {
		ParameterizedType pType = Cast.as(type, ParameterizedType.class);
		if (pType != null) {
			return pType.getActualTypeArguments()[i];
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", type, name);
	}
}
