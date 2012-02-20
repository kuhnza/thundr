package com.atomicleopard.webFramework.bind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class HttpServletRequestActionParameterBinder implements ActionParameterBinder<HttpServletRequest> {
	public HttpServletRequest bind(ParameterBinderFinder binderFinder, ActionParameter parameter, HttpServletRequest req, HttpServletResponse res) {
		return req;
	}
}
