package com.atomicleopard.webFramework.view.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.route.RouteNotFoundException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class RouteNotFoundViewResolver implements ViewResolver<RouteNotFoundException> {
	private HttpStatusExceptionViewResolver delegate = new HttpStatusExceptionViewResolver();

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, RouteNotFoundException viewResult) {
		delegate.resolve(req, resp, viewResult);
	}
}
