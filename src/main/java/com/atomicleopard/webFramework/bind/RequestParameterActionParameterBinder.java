package com.atomicleopard.webFramework.bind;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class RequestParameterActionParameterBinder implements ActionParameterBinder<Object> {
	public Object bind(ActionParameter parameter, HttpServletRequest req, HttpServletResponse resp) {
		String values = req.getParameter(parameter.name);
		if(parameter.isA(Collection.class)){
			return list()
		}
		req.getParameterValues(parameter.name)
		if(parameter.n)
		if (HttpServletRequest.class.isAssignableFrom(parameter.type.getClass())) {
			return req;
		}

		return null;
	}
	
}
