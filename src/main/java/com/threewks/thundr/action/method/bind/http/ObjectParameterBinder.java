package com.threewks.thundr.action.method.bind.http;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.introspection.MethodIntrospector;
import com.threewks.thundr.introspection.ParameterDescription;

public class ObjectParameterBinder implements ParameterBinder<Object> {
	private static Set<Class<?>> classesToSkip = createClassesToSkip();
	private MethodIntrospector methodIntrospector = new MethodIntrospector();

	public Object bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
		if (shouldProcess(parameterDescription)) {
			Constructor<?> ctor = findCtor(parameterDescription.classType());
			List<ParameterDescription> parameterDescriptions = methodIntrospector.getParameterDescriptions(ctor);
			List<?> parameters = binders.createFor(parameterDescriptions, pathMap.pathMapFor(parameterDescription.name()));
			try {
				return ctor.newInstance(parameters.toArray());
			} catch (Exception e) {
				throw new BindException(e, "Failed to bind onto %s: %s", ctor, e.getMessage());
			}
		}
		return null;
	}

	private boolean shouldProcess(ParameterDescription parameterDescription) {
		return !classesToSkip.contains(parameterDescription.classType());
	}

	private static Set<Class<?>> createClassesToSkip() {
		return new HashSet<Class<?>>(Expressive.<Class<?>> list(String.class, int.class, byte.class, short.class, float.class, double.class, long.class, void.class, Integer.class, Byte.class,
				Short.class, Float.class, Double.class, Long.class, Void.class));
	}

	private Constructor<?> findCtor(Class<?> type) {
		// TODO - Find the most appropraite ctor
		return type.getConstructors()[0];
	}

	@Override
	public boolean willBind(ParameterDescription parameterDescription) {
		return false;
	}

}
