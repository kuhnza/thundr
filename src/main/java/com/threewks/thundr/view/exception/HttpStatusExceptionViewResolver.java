package com.threewks.thundr.view.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class HttpStatusExceptionViewResolver implements ViewResolver<HttpStatusException> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, HttpStatusException viewResult) {
		try {
			resp.sendError(viewResult.getStatus());
		} catch (IOException e) {
			throw new ViewResolutionException(e, "Failed to send error status %d", viewResult.getStatus());
		}
	}
}
