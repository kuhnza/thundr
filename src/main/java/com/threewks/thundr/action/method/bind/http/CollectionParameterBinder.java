package com.threewks.thundr.action.method.bind.http;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.typeconverter.TypeConverterManager;
import jodd.util.ReflectUtil;

import com.threewks.thundr.collection.factory.CollectionFactory;
import com.threewks.thundr.introspection.ParameterDescription;

/**
 * Binds to a collection using the given Factory. Supports two types of list - indexed and unindexed.
 * An indexed list looks like this:
 * list[0]=value
 * list[1]=value
 * 
 * and unindexed list looks like this:
 * list=value,value
 * 
 */
public class CollectionParameterBinder<T extends Collection<Object>> implements ParameterBinder<T> {
	private static final Pattern indexPattern = Pattern.compile("\\[(\\d+)\\]");
	private CollectionFactory<T> collectionFactory;

	public CollectionParameterBinder(CollectionFactory<T> collectionFactory) {
		this.collectionFactory = collectionFactory;
	}

	public T bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] entryForParameter = pathMap.get(parameterDescription.name());
		entryForParameter = entryForParameter == null ? pathMap.get(parameterDescription.name() + "[]") : entryForParameter;
		boolean isIndexed = entryForParameter == null || entryForParameter.length == 0;
		return isIndexed ? createIndexed(binders, parameterDescription, pathMap) : createUnindexed(binders, parameterDescription, pathMap);
	}

	private T createUnindexed(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] entries = pathMap.get(parameterDescription.name());
		entries = entries == null ? pathMap.get(parameterDescription.name() + "[]") : entries;
		// a special case of a single empty string entry we'll equate to null
		if (entries == null || entries.length == 1 && (entries[0] == null || "".equals(entries[0]))) {
			return null;
		}
		T listParameter = collectionFactory.create();
		Type type = parameterDescription.getGenericType(0);
		Class<?> clazz = ReflectUtil.toClass(type);
		for (String entry : entries) {
			Object listEntry = ReflectUtil.castType(entry, clazz);
			listParameter.add(listEntry);
		}
		return listParameter;
	}

	private T createIndexed(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		pathMap = pathMap.pathMapFor(parameterDescription.name());
		Set<String> uniqueChildren = pathMap.uniqueChildren();
		if (uniqueChildren.size() == 0) {
			return null;
		}

		Map<Integer, String> keyToIndex = new HashMap<Integer, String>();
		int highestIndex = 0;
		for (String string : uniqueChildren) {
			Matcher matcher = indexPattern.matcher(string);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(String.format("Cannot bind %s%s - not a valid list index", parameterDescription.name(), string));
			}
			String indexString = matcher.group(1);
			int index = Integer.parseInt(indexString);
			keyToIndex.put(index, string);
			highestIndex = Math.max(highestIndex, index);
		}
		highestIndex += 1;

		T listParameter = collectionFactory.create();
		for (int i = 0; i < highestIndex; i++) {
			String key = keyToIndex.get(i);
			if (key != null) {
				ParameterDescription parameter = new ParameterDescription(key, parameterDescription.getGenericType(0));
				Object listEntry = binders.createFor(parameter, pathMap);
				listParameter.add(listEntry);
			} else {
				listParameter.add(null);
			}
		}
		return listParameter;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.isA(collectionFactory.forType());
	}

	@Override
	public String toString() {
		return this.getClass() + " for " + collectionFactory.forType();
	}
}
