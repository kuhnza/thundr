package com.atomicleopard.webFramework.bind;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class ListActionParameterBinder implements ActionParameterBinder<List<?>> {
	public List<?> bind(ParameterBinderFinder binderFinder, ActionParameter parameter, HttpServletRequest req, HttpServletResponse resp) throws BindException {
		Type type = parameter.getGenericType(0);
		ActionParameterBinder<?> delegateBinder = binderFinder.findBinder(type.getClass());
		req.getP
		new ActionParameter(name, type)
		delegateBinder.bind(binderFinder, parameter, req, resp)
		
		
		return req.getParameter(parameter.name);
	}
	protected List<?> createList(){
		return new ArrayList<Object>();
	}
}
