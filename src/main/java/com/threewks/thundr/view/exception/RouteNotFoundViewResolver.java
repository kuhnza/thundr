package com.threewks.thundr.view.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.route.RouteNotFoundException;
import com.threewks.thundr.view.ViewResolver;

public class RouteNotFoundViewResolver implements ViewResolver<RouteNotFoundException> {
	private HttpStatusExceptionViewResolver delegate = new HttpStatusExceptionViewResolver();

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, RouteNotFoundException viewResult) {
		delegate.resolve(req, resp, viewResult);
	}
}
