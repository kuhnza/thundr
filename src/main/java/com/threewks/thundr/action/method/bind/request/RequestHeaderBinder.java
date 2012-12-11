package com.threewks.thundr.action.method.bind.request;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.introspection.ParameterDescription;

public class RequestHeaderBinder implements ActionMethodBinder {
	private HttpBinder delegate;

	public RequestHeaderBinder(HttpBinder delegate) {
		this.delegate = delegate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		for (ParameterDescription parameterDescription : bindings.keySet()) {
			if (bindings.get(parameterDescription) == null) {
				Enumeration<String> headerNames = req.getHeaderNames();
				if (headerNames != null) {
					Map<String, String[]> headerValues = new HashMap<String, String[]>();
					for (String header : Expressive.<String> iterable(headerNames)) {
						headerValues.put(header, headerValues(req.getHeaders(header)));
					}

					delegate.bind(bindings, req, resp, headerValues);
				}
			}
		}
	}

	private String[] headerValues(Enumeration<String> headers) {
		return list(Expressive.iterable(headers)).toArray(new String[0]);
	}
}
