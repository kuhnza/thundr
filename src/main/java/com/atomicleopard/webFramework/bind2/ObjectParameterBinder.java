package com.atomicleopard.webFramework.bind2;

import static com.atomicleopard.expressive.Expressive.set;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.webFramework.bind.BindException;
import com.atomicleopard.webFramework.bind.Binders;
import com.atomicleopard.webFramework.introspection.MethodIntrospector;
import com.atomicleopard.webFramework.introspection.ParameterDescription;

public class ObjectParameterBinder implements ParameterBinder<Object> {
	private static Set<Class<?>> classesToSkip = createClassesToSkip();
	private MethodIntrospector methodIntrospector = new MethodIntrospector();

	public Object bind(Binders binders, ParameterDescription parameterDescription, PathMap pathMap) {
		if (shouldProcess(parameterDescription)) {
			Constructor<?> ctor = findCtor(parameterDescription.type());
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
		return !classesToSkip.contains(parameterDescription.type());
	}

	private static Set<Class<?>> createClassesToSkip() {
		return new HashSet<Class<?>>(Expressive.<Class<?>> list(String.class, int.class, byte.class, short.class, float.class, double.class, long.class, void.class, Integer.class, Byte.class,
				Short.class, Float.class, Double.class, Long.class, Void.class));
	}

	private Constructor<?> findCtor(Class<?> type) {
		// TODO - Find the most appropraite ctor
		return type.getConstructors()[0];
	}

}
