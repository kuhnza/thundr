package com.atomicleopard.webFramework.introspection;

import java.lang.reflect.Type;

import jodd.util.ReflectUtil;

import com.atomicleopard.webFramework.logger.Logger;

public class ParameterDescription {
	private String name;
	private Type type;

	public ParameterDescription(String name, Type type) {
		super();
		this.name = name;
		this.type = type;
	}

	public boolean isA(Class<?> is) {
		Class<?> clazz = type();
		if (clazz == null) {
			Logger.warn("Clazz is null, type is %s", type);
		}

		return clazz == null ? false : is.isAssignableFrom(clazz);
	}

	public Type getGenericType(int i) {
		return ReflectUtil.getComponentType(type, i);
	}

	public boolean isGeneric() {
		return ReflectUtil.getComponentType(type) != null;
	}

	public String name() {
		return name;
	}

	public Class<?> type() {
		return ReflectUtil.toClass(type);
	}

	@Override
	public String toString() {
		return String.format("%s %s", type, name);
	}
}
