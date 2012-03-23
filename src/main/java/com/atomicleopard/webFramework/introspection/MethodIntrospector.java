package com.atomicleopard.webFramework.introspection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;

import com.atomicleopard.expressive.Cast;

public class MethodIntrospector {
	public List<ParameterDescription> getParameterDescriptions(AccessibleObject methodOrCtor) {
		MethodParameter[] parameterNames = Paramo.resolveParameters(methodOrCtor);
		Type[] genericParameterTypes = getGenericParameterTypes(methodOrCtor);
		return mergeToParameters(parameterNames, genericParameterTypes);
	}

	private List<ParameterDescription> mergeToParameters(MethodParameter[] parameterNames, Type[] types) {
		List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
		for (int i = 0; i < types.length; i++) {
			parameters.add(new ParameterDescription(parameterNames[i].getName(), types[i]));
		}
		return parameters;
	}

	private Type[] getGenericParameterTypes(AccessibleObject methodOrCtor) {
		Method method = Cast.as(methodOrCtor, Method.class);
		Constructor<?> ctor = Cast.as(methodOrCtor, Constructor.class);
		return method != null ? method.getGenericParameterTypes() : ctor.getGenericParameterTypes();
	}
}
