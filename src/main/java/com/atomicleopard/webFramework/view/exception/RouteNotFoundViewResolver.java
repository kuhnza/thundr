package com.atomicleopard.webFramework.view.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.exception.BaseException;
import com.atomicleopard.webFramework.route.RouteException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class RouteNotFoundViewResolver implements ViewResolver<RouteException> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, RouteException viewResult) {
		try {
			resp.sendError(404, viewResult.getMessage());
		} catch (IOException e) {
			throw new BaseException(e);
		}
	}
}
