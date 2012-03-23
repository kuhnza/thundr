package com.atomicleopard.webFramework.bind;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.bind2.ListParameterBinder;
import com.atomicleopard.webFramework.bind2.ObjectParameterBinder;
import com.atomicleopard.webFramework.bind2.ParameterBinder;
import com.atomicleopard.webFramework.bind2.PathMap;
import com.atomicleopard.webFramework.bind2.StringParameterBinder;
import com.atomicleopard.webFramework.introspection.ParameterDescription;
import com.atomicleopard.webFramework.routes.ActionMethod;

public class Binders {
	private static Map<Class<?>, ParameterBinder<?>> sharedBinders = binderMap();
	private LinkedHashMap<Class<?>, ParameterBinder<?>> binders = new LinkedHashMap<Class<?>, ParameterBinder<?>>();

	public Binders addBinder(Class<?> forType, ParameterBinder<?> binder) {
		binders.put(forType, binder);
		return this;
	}

	public Binders addDefaultBinders() {
		binders.putAll(sharedBinders);
		return this;
	}

	public List<Object> createFor(List<ParameterDescription> parameterDescriptions, PathMap pathMap) {
		List<Object> parameters = new ArrayList<Object>(parameterDescriptions.size());
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			parameters.add(createFor(parameterDescription, pathMap));
		}
		return parameters;
	}

	public Object createFor(ParameterDescription parameterDescription, PathMap pathMap) {
		// do a direct lookup - we might get lucky!
		Object value = lookupBinder(parameterDescription, pathMap);
		return value == null ? tryAllBinders(parameterDescription, pathMap) : value;
	}

	private Object tryAllBinders(ParameterDescription parameterDescription, PathMap pathMap)  {
		for (Map.Entry<Class<?>, ParameterBinder<?>> binderEntry : binders.entrySet()) {
			if (parameterDescription.isA(binderEntry.getKey())) {
				Object value = binderEntry.getValue().bind(this, parameterDescription, pathMap);
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}

	private Object lookupBinder(ParameterDescription parameterDescription, PathMap pathMap)  {
		ParameterBinder<?> binder = binders.get(parameterDescription.type());
		return binder != null ? binder.bind(this, parameterDescription, pathMap) : null;
	}

	private static Map<Class<?>, ParameterBinder<?>> binderMap() {
		LinkedHashMap<Class<?>, ParameterBinder<?>> map = new LinkedHashMap<Class<?>, ParameterBinder<?>>();
		map.put(String.class, new StringParameterBinder());
		map.put(List.class, new ListParameterBinder());
		map.put(Object.class, new ObjectParameterBinder());
		return map;
	}
}
