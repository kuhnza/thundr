package com.threewks.thundr.action.method.bind.request;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestClassBinder implements ActionMethodBinder {
	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (Map.Entry<ParameterDescription, Object> binding : bindings.entrySet()) {
			if (binding.getValue() == null) {
				ParameterDescription parameterDescription = binding.getKey();

				Object value = null;
				if (parameterDescription.isA(HttpServletRequest.class)) {
					value = req;
				}
				if (parameterDescription.isA(HttpServletResponse.class)) {
					value = resp;
				}
				if (parameterDescription.isA(HttpSession.class)) {
					value = req.getSession();
				}
				bindings.put(parameterDescription, value);
			}
		}
	}
}
