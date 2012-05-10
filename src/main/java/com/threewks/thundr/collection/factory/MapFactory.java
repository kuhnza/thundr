package com.threewks.thundr.collection.factory;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface MapFactory<T extends Map> {

	Class<T> forType();

	T create();
}
