package com.atomicleopard.webFramework.collection.factory;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface MapFactory<T extends Map> {

	Class<T> forType();

	T create();
}
