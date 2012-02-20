package com.atomicleopard.webFramework.bind;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.routes.ActionParameter;

public class HttpServletResponseActionParameterBinder implements ActionParameterBinder<HttpServletResponse> {
	public HttpServletResponse bind(ParameterBinderFinder binderFinder, ActionParameter parameter, HttpServletRequest req, HttpServletResponse resp) {
		return resp;
	}
}
