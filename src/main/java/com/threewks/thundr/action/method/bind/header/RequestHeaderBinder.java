package com.threewks.thundr.action.method.bind.header;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.typeconverter.TypeConverterManager;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestHeaderBinder implements ActionMethodBinder {

	public static final List<Class<?>> PathVariableTypes = Arrays.<Class<?>>
			asList(String.class, int.class, Integer.class, double.class, Double.class, long.class, Long.class, short.class,
					Short.class, float.class, Float.class, BigDecimal.class, BigInteger.class);

	@SuppressWarnings("unchecked")
	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (ParameterDescription parameterDescription : bindings.keySet()) {
			if (bindings.get(parameterDescription) == null) {

				Map<String, List<String>> headerValues = new HashMap<String, List<String>>();
				for (String header : Expressive.<String> iterable(req.getHeaderNames())) {
					headerValues.put(header, headerValues(req.getHeaders(header)));
				}

				if (canBindFromPathVariable(parameterDescription)) {
					Object value = bind(parameterDescription, headerValues);
					bindings.put(parameterDescription, value);
				}
			}
		}
	}

	private List<String> headerValues(Enumeration<String> headers) {
		List<String> results = new ArrayList<String>();
		while (headers.hasMoreElements()) {
			results.add(headers.nextElement());
		}
		return results;
	}

	private boolean canBindFromPathVariable(ParameterDescription parameterDescription) {
		return PathVariableTypes.contains(parameterDescription.type());
	}

	private Object bind(ParameterDescription parameterDescription, Map<String, List<String>> headerValues) {
		List<String> values = headerValues.get(parameterDescription.name());
		String first = values == null ? null : values.get(0);
		return first == null ? null : TypeConverterManager.lookup(parameterDescription.classType()).convert(first);
	}
}
