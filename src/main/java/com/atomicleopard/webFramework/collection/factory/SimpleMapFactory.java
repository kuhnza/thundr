package com.atomicleopard.webFramework.collection.factory;

import java.util.Map;

import com.atomicleopard.webFramework.exception.BaseException;

@SuppressWarnings("rawtypes")
public class SimpleMapFactory<T extends Map> implements MapFactory<T> {

	private Class<T> type;
	private Class<?> instanceType;

	public SimpleMapFactory(Class<T> type, Class<?> instanceType) {
		this.type = type;
		this.instanceType = instanceType;
	}

	@Override
	public Class<T> forType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T create() {
		try {
			return (T) instanceType.newInstance();
		} catch (Exception e) {
			throw new BaseException(e, "Failed to instantiate a collection of type %s", type);
		}
	}

}
