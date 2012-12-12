package com.threewks.thundr.action.method.bind.http;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.typeconverter.TypeConverterManager;
import jodd.util.ReflectUtil;

import com.threewks.thundr.introspection.ParameterDescription;

/**
 * Binds to an array.
 * 
 * Supports two types of array - indexed and unindexed.
 * An indexed array looks like this:
 * list[0]=value
 * list[1]=value
 * 
 * and unindexed array looks like this:
 * list=value,value
 * 
 */
public class ArrayParameterBinder<T> implements ParameterBinder<T[]> {
	private static final Pattern indexPattern = Pattern.compile("\\[(\\d+)\\]");

	public T[] bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] entryForParameter = pathMap.get(parameterDescription.name());
		boolean isIndexed = entryForParameter == null || entryForParameter.length == 0;
		return isIndexed ? createIndexed(binders, parameterDescription, pathMap) : createUnindexed(binders, parameterDescription, pathMap);
	}

	@SuppressWarnings("unchecked")
	private T[] createUnindexed(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		String[] entries = pathMap.get(parameterDescription.name());
		// a special case of a single empty string entry we'll equate to null
		if (entries == null || entries.length == 1 && (entries[0] == null || "".equals(entries[0]))) {
			return null;
		}
		Type type = parameterDescription.getArrayType();
		Class<T> clazz = ReflectUtil.toClass(type);
		T[] arrayParameter = createArray(entries.length, clazz);
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
			arrayParameter[i] = TypeConverterManager.convertType(entry, clazz);
		}
		return arrayParameter;
	}

	@SuppressWarnings("unchecked")
	private T[] createIndexed(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
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
				throw new IllegalArgumentException(String.format("Cannot bind %s%s - not a valid array index", parameterDescription.name(), string));
			}
			String indexString = matcher.group(1);
			int index = Integer.parseInt(indexString);
			keyToIndex.put(index, string);
			highestIndex = Math.max(highestIndex, index);
		}
		highestIndex += 1;

		Type type = parameterDescription.getArrayType();
		Class<T> clazz = ReflectUtil.toClass(type);
		T[] arrayParameter = createArray(highestIndex, clazz);
		for (int i = 0; i < highestIndex; i++) {
			String key = keyToIndex.get(i);
			if (key != null) {
				ParameterDescription parameter = new ParameterDescription(key, type);
				T listEntry = (T) binders.createFor(parameter, pathMap);
				arrayParameter[i] = listEntry;
			}
		}
		return arrayParameter;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.classType().isArray();
	}

	@SuppressWarnings("unchecked")
	private T[] createArray(int size, Class<T> clazz) {
		return (T[]) Array.newInstance(clazz, size);
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
