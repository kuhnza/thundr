package com.threewks.thundr.collection.factory;

import java.util.Collection;

import com.threewks.thundr.exception.BaseException;

@SuppressWarnings("rawtypes")
public class SimpleCollectionFactory<T extends Collection> implements CollectionFactory<T> {

	private Class<T> type;
	private Class<?> instanceType;

	public SimpleCollectionFactory(Class<T> type, Class<?> instanceType) {
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
