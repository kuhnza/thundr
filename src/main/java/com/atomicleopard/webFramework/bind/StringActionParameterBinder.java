package com.atomicleopard.webFramework.bind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class StringActionParameterBinder implements ActionParameterBinder<String> {
	public String bind(ParameterBinderFinder binderFinder, ActionParameter parameter, HttpServletRequest req, HttpServletResponse res) {
		return req.getParameter(parameter.name);
	}
}
