package com.atomicleopard.webFramework.bind;

import java.lang.reflect.Type;

import jodd.util.ReflectUtil;

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
		if (clazz == null) {
			Logger.warn("Clazz is null, type is %s", type);
		}

		return clazz == null ? false : is.isAssignableFrom(clazz);
	}

	public Type getGenericType(int i) {
		return ReflectUtil.getComponentType(type, i);
	}

	@Override
	public String toString() {
		return String.format("%s %s", type, name);
	}
}
