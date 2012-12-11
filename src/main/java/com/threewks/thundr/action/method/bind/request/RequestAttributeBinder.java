package com.threewks.thundr.action.method.bind.request;

import static com.atomicleopard.expressive.Expressive.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestAttributeBinder implements ActionMethodBinder {
	private HttpBinder delegate;

	public RequestAttributeBinder(HttpBinder delegate) {
		this.delegate = delegate;
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
			ParameterDescription key = binding.getKey();
			String name = key.name();
			Object value = req.getAttribute(name);
			if (binding.getValue() == null && value != null) {
				if (key.isA(value.getClass())) {
					bindings.put(key, value);
				}
				else if (value instanceof String) {
					String stringValue = (String) value;
					Map<String, String[]> parameterMap = map(name, array(stringValue));
					delegate.bind(bindings, req, resp, parameterMap);
				}
			}
		}
	}
}
