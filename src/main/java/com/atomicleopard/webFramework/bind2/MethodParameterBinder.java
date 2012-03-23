package com.atomicleopard.webFramework.bind2;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jodd.util.ReflectUtil;

import com.atomicleopard.webFramework.introspection.MethodIntrospector;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class MethodParameterBinder {
	// TODO - this should probably sort (or self sort) based on the inheritence hierarchy so that more relevant binders come first
	private static Map<Class<?>, ParameterBinder<?>> binders = binderMap();
	private static MethodIntrospector methodIntrospector = new MethodIntrospector();

	public Object invoke(Object instance, Method method, Map<String, String[]> requestParams) throws Exception {
		PathMap pathMap = new PathMap(requestParams);
		List<Object> params = createParameters(method, pathMap);
		Object result = method.invoke(instance, params.toArray());
		return result;
	}

	public Object createParameter(ParameterDescription parameter, PathMap pathMap) throws Exception {
		Type type = parameter.type();
		ParameterBinder<?> binder = findBinder(type);
		return binder == null ? null : binder.bind(this, parameter, pathMap);
	}

	public List<Object> createParameters(AccessibleObject methodOrCtor, PathMap pathMap) throws Exception {
		List<ParameterDescription> parameters = methodIntrospector.getParameterDescriptions(methodOrCtor);
		List<Object> result = new ArrayList<Object>(parameters.size());
		for (ParameterDescription parameter : parameters) {
			Object param = createParameter(parameter, pathMap);
			result.add(param);
		}
		return result;
	}

	private ParameterBinder<?> findBinder(Type type) {
		Class<?> clazz = ReflectUtil.toClass(type);
		ParameterBinder<?> binder = binders.get(clazz);
		if (binder == null && clazz != null) {
			for (Map.Entry<Class<?>, ParameterBinder<?>> binderEntry : binders.entrySet()) {
				if (binderEntry.getKey().isAssignableFrom(clazz)) {
					binder = binderEntry.getValue();
					break;
				}
			}
		}
		return binder;
	}

	private static Map<Class<?>, ParameterBinder<?>> binderMap() {
		LinkedHashMap<Class<?>, ParameterBinder<?>> map = new LinkedHashMap<Class<?>, ParameterBinder<?>>();
		map.put(String.class, new StringParameterBinder());
		map.put(List.class, new ListParameterBinder());
		map.put(Object.class, new ObjectParameterBinder());
		return map;
	}
}
