package com.threewks.thundr.introspection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jodd.util.ReflectUtil;

import com.atomicleopard.expressive.Cast;
import com.threewks.thundr.logger.Logger;

public class ParameterDescription {
	private String name;
	private Type type;

	public ParameterDescription(String name, Type type) {
		super();
		this.name = name;
		this.type = type;
	}

	public boolean isA(Class<?> is) {
		Class<?> clazz = classType();
		if (clazz == null) {
			Logger.warn("Clazz is null, type is %s", type);
		}

		return clazz == null ? false : clazz.isAssignableFrom(is);
	}

	public Type getGenericType(int index) {
		ParameterizedType pt = Cast.as(type, ParameterizedType.class);
		if (pt != null) {
			Type[] generics = pt.getActualTypeArguments();
			if (index < 0) {
				index = generics.length + index;
			}
			if (index < generics.length) {
				return generics[index];
			}
		}
		return null;
	}

	public Type getArrayType() {
		Class<?> clazz = Cast.as(type, Class.class);
		if (clazz != null) {
			return clazz.getComponentType();
		}
		GenericArrayType gat = Cast.as(type, GenericArrayType.class);
		if (gat != null) {
			return gat.getGenericComponentType();
		}
		return null;
	}

	public boolean isGeneric() {
		return ReflectUtil.getComponentType(type) != null;
	}

	public String name() {
		return name;
	}

	public Class<?> classType() {
		return ReflectUtil.toClass(type);
	}

	public Type type() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("%s %s", type, name);
	}
}
