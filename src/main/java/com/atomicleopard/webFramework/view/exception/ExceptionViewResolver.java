package com.atomicleopard.webFramework.view.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atomicleopard.webFramework.logger.Logger;
import com.atomicleopard.webFramework.view.ViewResolutionException;
import com.atomicleopard.webFramework.view.ViewResolver;

public class ExceptionViewResolver implements ViewResolver<Throwable> {
	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, Throwable viewResult) {
		List<String> messages = new ArrayList<String>();
		for (Throwable cause = viewResult; cause != null; cause = cause.getCause()) {
			messages.add(cause.getMessage());
		}
		try {
			Throwable exceptionOfInterest = viewResult instanceof ViewResolutionException ? viewResult.getCause() : viewResult;
			PrintWriter writer = resp.getWriter();
			for (String message : messages) {
				writer.println(message);
			}
			exceptionOfInterest.printStackTrace(writer);
			// to output a useful page, you can't send internal server error
			// this probably should be a 'debug' mode thing, or configurable
			// TODO - resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			exceptionOfInterest.printStackTrace(System.out);
		} catch (IOException e) {
			Logger.error("Failed to render an exception view because '%s' - original exception: %s", e.getMessage(), viewResult.getMessage());
			viewResult.printStackTrace();
		}
	}
}
