/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.collection.Pair;
import com.atomicleopard.expressive.collection.Triplets;
import com.threewks.thundr.configuration.Environment;
import com.threewks.thundr.introspection.ClassIntrospector;
import com.threewks.thundr.introspection.MethodIntrospector;
import com.threewks.thundr.introspection.ParameterDescription;

public class InjectionContextImpl implements UpdatableInjectionContext {
	private static final String ENVIRONMENT_SEPARATOR = "%";
	private static final Set<Class<?>> TypesRequiringAName = createListOfTypesRequiringAName();

	private Triplets<Class<?>, String, Class<?>> types = map();
	private Triplets<Class<?>, String, Object> instances = map();

	private MethodIntrospector methodIntrospector = new MethodIntrospector();
	private ClassIntrospector classIntrospector = new ClassIntrospector();

	@Override
	public <T> InjectorBuilder<T> inject(Class<T> type) {

		if (!ClassIntrospector.isABasicType(type) && (type.isInterface() || Modifier.isAbstract(type.getModifiers()))) {
			throw new InjectionException("Unable to inject the type '%s' - you cannot inject interfaces or abstract classes", type.getName());
		}

		return new InjectorBuilder<T>(this, type);
	}

	@Override
	public <T> InjectorBuilder<T> inject(T instance) {
		return new InjectorBuilder<T>(this, instance);
	}

	@Override
	public <T> T get(Class<T> type) {
		T instance = getExistingInstance(type, null);
		if (instance == null) {
			instance = createAndAddInstance(type, null);
		}
		if (instance == null) {
			instance = getOnlyExistingNamedInstanceForNonBasicType(type);
		}
		return instance;
	}

	@Override
	public <T> T get(Class<T> type, String name) {
		T instance = getExistingInstance(type, name);
		if (instance == null) {
			instance = createAndAddInstance(type, name);
		}
		if (instance == null) {
			instance = get(type);
		}
		return instance;
	}

	protected <T> void addType(Class<T> type, String name, Class<? extends T> as) {
		types.put(type, name, as);
	}

	protected <T> void addInstance(Class<T> type, String name, T as) {
		instances.put(type, name, as);
	}

	@SuppressWarnings("unchecked")
	private <T> T createAndAddInstance(Class<T> type, String name) {
		T instance = null;
		T newInstance = instantiate((Class<T>) types.get(type, name));
		if (newInstance != null) {
			synchronized (instances) {
				if (!instances.containsKey(type, name)) {
					instances.put(type, name, newInstance);
				}
				instance = (T) instances.get(type, name);
			}
		}
		return instance;
	}

	private <T> T instantiate(Class<T> type) {
		if (type == null) {
			return null;
		}
		List<Constructor<T>> ctors = classIntrospector.listConstructors(type);
		List<ParameterDescription> minimalParameters = Collections.emptyList();
		for (int i = ctors.size() - 1; i >= 0; i--) {
			Constructor<T> constructor = ctors.get(i);
			List<ParameterDescription> parameterDescriptions = methodIntrospector.getParameterDescriptions(constructor);
			minimalParameters = parameterDescriptions;
			if (canSatisfy(parameterDescriptions)) {
				Object[] args = getAll(parameterDescriptions);
				T instance = invokeConstructor(constructor, args);
				instance = invokeSetters(type, instance);
				return setFields(type, instance);
			}
		}

		throw new InjectionException("Could not create a %s - cannot match parameters of any available constructors. The minimal set of parameters required is %s", type.getName(), minimalParameters);
	}

	private <T> T invokeSetters(Class<T> type, T instance) {
		List<Method> setters = classIntrospector.listSetters(type);
		for (Method method : setters) {
			String name = getPropertyNameFromSetMethod(method);
			try {
				Class<?> argumentType = method.getParameterTypes()[0];
				if (contains(argumentType, name)) {
					method.invoke(instance, get(argumentType, name));
				}
			} catch (Exception e) {
				throw new InjectionException(e, "Failed to inject into %s.%s: %s", type.getName(), method.getName(), getRootMessage(e));
			}
		}
		return instance;
	}

	// TODO - Stack Overflow - A thread local storing types being created could bail
	// out early in the case of stack overflow
	private <T> T setFields(Class<T> type, T instance) {
		List<Field> fields = classIntrospector.listInjectionFields(type);
		for (Field field : fields) {
			try {
				Object beanProperty = get(field.getType(), field.getName());
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.set(instance, beanProperty);
				field.setAccessible(accessible);
			} catch (Exception e) {
				throw new InjectionException(e, "Failed to inject into %s.%s: %s", type.getName(), field.getName(), getRootMessage(e));
			}
		}

		return instance;
	}

	private Object[] getAll(List<ParameterDescription> parameterDescriptions) {
		List<Object> args = new ArrayList<Object>(parameterDescriptions.size());
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			Object arg = get(parameterDescription.classType(), parameterDescription.name());
			args.add(arg);
		}
		return args.toArray();
	}

	@Override
	public <T> boolean contains(Class<T> type) {
		return contains(type, null);
	}

	@Override
	public <T> boolean contains(Class<T> type, String name) {
		boolean contains = false;
		if (name != null) {
			String envName = environmentSpecificName(name);
			// named or environment named instance
			contains = contains || instances.containsKey(type, envName) || instances.containsKey(type, name);
			// named or environment named type
			contains = contains || types.containsKey(type, envName) || types.containsKey(type, name);
		}
		// unnamed instance
		contains = contains || instances.containsKey(type, null);
		// unnamed type
		contains = contains || types.containsKey(type, null);
		return contains;
	}

	@Override
	public String toString() {
		return String.format("Injection context (%s instances, %s classes)", instances.size(), types.size());
	}

	@SuppressWarnings("unchecked")
	private <T> T getExistingInstance(Class<T> type, String name) {
		String environmentSpecificName = environmentSpecificName(name);
		T instance = (T) instances.get(type, environmentSpecificName);
		if (instance == null) {
			instance = (T) instances.get(type, name);
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	private <T> T getOnlyExistingNamedInstanceForNonBasicType(Class<T> type) {
		boolean isBasicType = TypesRequiringAName.contains(type);
		if (!isBasicType) {
			Map<String, T> existing = new HashMap<String, T>();
			for (Entry<Pair<Class<?>, String>, Object> entry : instances.entrySet()) {
				Pair<Class<?>, String> key = entry.getKey();
				if (type.equals(key.getA())) {
					T t = (T) entry.getValue();
					existing.put(key.getB(), t);
				}
			}
			if (existing.size() > 1) {
				throw new InjectionException("Unable to get an instance of %s - the result is ambiguous. The following matches exist: %s", type.getName(), StringUtils.join(existing.keySet(), ", "));
			}
			if (existing.size() == 1) {
				return existing.values().iterator().next();
			}
		}
		return null;
	}

	private String environmentSpecificName(String name) {
		return name + ENVIRONMENT_SEPARATOR + Environment.get();
	}

	private boolean canSatisfy(List<ParameterDescription> parameterDescriptions) {
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			if (get(parameterDescription.classType(), parameterDescription.name()) == null) {
				return false;
			}
		}
		return true;
	}

	private <T> T invokeConstructor(Constructor<T> constructor, Object[] args) {
		try {
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new InjectionException(e, "Failed to create a new instance using the constructor %s: %s", constructor.toString(), getRootMessage(e));
		}
	}

	private String getRootMessage(Exception e) {
		Throwable rootCause = ExceptionUtils.getRootCause(e);
		String message = rootCause == null ? e.getMessage() : rootCause.getMessage();
		return message;
	}

	private String getPropertyNameFromSetMethod(Method method) {
		String nameWithUpperCaseFirstLetter = method.getName().replace("set", "");
		return nameWithUpperCaseFirstLetter.substring(0, 1).toLowerCase() + nameWithUpperCaseFirstLetter.substring(1);
	}

	private <K1, K2, V> Triplets<K1, K2, V> map() {
		return new Triplets<K1, K2, V>();
	}

	private static Set<Class<?>> createListOfTypesRequiringAName() {
		return Expressive.<Class<?>> set(String.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class,
				Double.class, char.class, Character.class, boolean.class, Boolean.class, BigDecimal.class, BigInteger.class, List.class, Set.class, Map.class, Collection.class);
	}
}
