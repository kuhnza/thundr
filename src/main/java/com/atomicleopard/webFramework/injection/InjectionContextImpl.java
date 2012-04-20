package com.atomicleopard.webFramework.injection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.bean.BeanUtil;
import jodd.bean.BeanUtilUtil;
import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.introspection.ClassIntrospector;
import com.atomicleopard.webFramework.introspection.MethodIntrospector;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class InjectionContextImpl implements UpdatableInjectionContext {
	private Map<Class<?>, Class<?>> types = map();
	private Map<Class<?>, Object> instances = map();
	private Map<NamedType<?>, Class<?>> namedTypes = map();
	private Map<NamedType<?>, Object> namedInstances = map();

	private MethodIntrospector methodIntrospector = new MethodIntrospector();
	private ClassIntrospector classIntrospector = new ClassIntrospector();

	@Override
	public <T> InjectorBuilder<T> inject(Class<T> type) {
		return new InjectorBuilder<T>(this, type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T inject(T instance) {
		Class<T> class1 = (Class<T>) instance.getClass();
		addInstance(class1, null, instance);
		return instance;
	};

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type) {
		T instance = (T) instances.get(type);
		if (instance == null) {
			instance = instantiate((Class<T>) types.get(type));
		}
		if (instance == null) {
			throw new NullPointerException(String.format("Could not inject '%s' - no dependency configured for injection", type.getName()));
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, String name) {
		NamedType<T> namedType = new NamedType<T>(type, name);
		T instance = (T) namedInstances.get(namedType);
		instance = instance == null ? (T) instantiate(namedTypes.get(namedType)) : instance;
		return instance == null ? get(type) : instance;
	}

	protected <T> void addType(Class<T> type, String name, Class<? extends T> as) {
		if (StringUtils.isNotBlank(name)) {
			namedTypes.put(new NamedType<T>(type, name), as);
		} else {
			types.put(type, as);
		}
	}

	protected <T> void addInstance(Class<T> type, String name, T as) {
		if (StringUtils.isNotBlank(name)) {
			namedInstances.put(new NamedType<T>(type, name), as);
		} else {
			instances.put(type, as);
		}
	}

	private <T> T instantiate(Class<T> type) {
		if (type == null) {
			return null;
		}
		List<Constructor<T>> ctors = classIntrospector.listConstructors(type);
		for (int i = ctors.size() - 1; i >= 0; i--) {
			Constructor<T> constructor = ctors.get(i);
			List<ParameterDescription> parameterDescriptions = methodIntrospector.getParameterDescriptions(constructor);
			if (canSatisfy(parameterDescriptions)) {
				Object[] args = getAll(parameterDescriptions);
				T instance = invokeConstructor(constructor, args);
				instance = invokeSetters(type, instance);
				return setFields(type, instance);
			}
		}

		throw new InjectionException("Could not create a %s - cannot match parameters of any available constructors", type.getName());
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
				throw new InjectionException(e, "Failed to inject into %s.%s: %s", type.getName(), method.getName(), e.getMessage());
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
				throw new InjectionException(e, "Failed to inject into %s.%s: %s", type.getName(), field.getName(), e.getMessage());
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
		return instances.containsKey(type) || types.containsKey(type);
	}

	@Override
	public <T> boolean contains(Class<T> type, String name) {
		NamedType<T> namedType = new NamedType<T>(type, name);
		return namedInstances.containsKey(namedType) || namedTypes.containsKey(namedType) || contains(type);
	}

	@Override
	public String toString() {
		return String.format("Injection context (%s instances, %s classes)", instances.size() + namedInstances.size(), types.size() + namedTypes.size());
	}

	private boolean canSatisfy(List<ParameterDescription> parameterDescriptions) {
		for (ParameterDescription parameterDescription : parameterDescriptions) {
			if (!contains(parameterDescription.classType(), parameterDescription.name())) {
				return false;
			}
		}
		return true;
	}

	private <T> T invokeConstructor(Constructor<T> constructor, Object[] args) {
		try {
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new BaseException(e, "Failed to create a new instance using the constructor %s: %s", constructor.getName(), e.getMessage());
		}
	}

	private String getPropertyNameFromSetMethod(Method method) {
		String nameWithUpperCaseFirstLetter = method.getName().replace("set", "");
		return nameWithUpperCaseFirstLetter.substring(0, 1).toLowerCase() + nameWithUpperCaseFirstLetter.substring(1);
	}

	private <K, V> HashMap<K, V> map() {
		return new HashMap<K, V>();
	}

}
