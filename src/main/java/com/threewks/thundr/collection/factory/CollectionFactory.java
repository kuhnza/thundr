package com.threewks.thundr.collection.factory;

import java.util.Collection;

@SuppressWarnings("rawtypes")
public interface CollectionFactory<T extends Collection> {

	Class<T> forType();

	T create();
}
