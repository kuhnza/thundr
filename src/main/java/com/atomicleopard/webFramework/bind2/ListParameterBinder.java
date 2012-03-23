package com.atomicleopard.webFramework.bind2;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.util.ReflectUtil;

import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

/**
 * Binds to a list. Supports two types of list - indexed and unindexed.
 * An indexed list looks like this:
 * list[0]=value
 * list[1]=value
 * 
 * and unindexed list looks like this:
 * list=value,value
 * 
 */
public class ListParameterBinder implements ParameterBinder<List<?>> {
	private static final Pattern indexPattern = Pattern.compile("\\[(\\d+)\\]");

	public List<?> bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		String[] entryForParameter = pathMap.get(parameterDescription.name());
		boolean indexedList = entryForParameter == null || entryForParameter.length == 0;
		return indexedList ? createIndexedList(binders, parameterDescription, pathMap) : createUnindexList(binders, parameterDescription, pathMap);
	}

	private List<?> createUnindexList(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		String[] entries = pathMap.get(parameterDescription.name());
		List<Object> listParameter = new ArrayList<Object>(entries.length);
		Type type = parameterDescription.getGenericType(0);
		Class<?> clazz = ReflectUtil.toClass(type);
		for (String entry : entries) {
			Object listEntry = ReflectUtil.castType(entry, clazz);
			listParameter.add(listEntry);
		}
		return listParameter;
	}

	private List<?> createIndexedList(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		pathMap = pathMap.pathMapFor(parameterDescription.name());
		Set<String> uniqueChildren = pathMap.uniqueChildren();

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

		List<Object> listParameter = new ArrayList<Object>(highestIndex);
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
}
