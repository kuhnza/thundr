package com.atomicleopard.webFramework.bind2;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.collection.factory.MapFactory;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

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
public class MapParameterBinder<T extends Map<Object, Object>> implements ParameterBinder<T> {
	private static final Pattern elementPattern = Pattern.compile("\\[(.+)\\]");
	private MapFactory<T> mapFactory;

	public MapParameterBinder(MapFactory<T> mapFactory) {
		this.mapFactory = mapFactory;
	}

	public T bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		pathMap = pathMap.pathMapFor(parameterDescription.name());
		Set<String> children = pathMap.uniqueChildren();
		if (children.isEmpty()) {
			return null;
		}

		T mapParameter = mapFactory.create();
		for (String string : children) {
			Matcher matcher = elementPattern.matcher(string);
			if (!matcher.matches()) {
				throw new IllegalArgumentException(String.format("Cannot bind %s%s - not a valid map key", parameterDescription.name(), string));
			}
			String name = matcher.group(1);
			Type genericType = parameterDescription.getGenericType(1);
			ParameterDescription parameter = new ParameterDescription(string, genericType);
			Object mapEntry = binders.createFor(parameter, pathMap);
			mapParameter.put(name, mapEntry);
		}
		return mapParameter;
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return parameterDescription.isA(mapFactory.forType());
	}

	@Override
	public String toString() {
		return this.getClass() + " for " + mapFactory.forType();
	}
}
