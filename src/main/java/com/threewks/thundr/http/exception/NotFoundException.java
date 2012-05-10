package com.threewks.thundr.http.exception;

import javax.servlet.http.HttpServletResponse;

public class NotFoundException extends HttpStatusException {
	private static final long serialVersionUID = 2396527384580755236L;

	public NotFoundException(String format, Object... formatArgs) {
		super(HttpServletResponse.SC_NOT_FOUND, format, formatArgs);
	}

	public NotFoundException(Throwable cause, String format, Object... formatArgs) {
		super(cause, HttpServletResponse.SC_NOT_FOUND, format, formatArgs);
	}
}
