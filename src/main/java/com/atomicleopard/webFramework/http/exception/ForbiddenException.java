package com.atomicleopard.webFramework.http.exception;

import javax.servlet.http.HttpServletResponse;

public class ForbiddenException extends HttpStatusException {
	private static final long serialVersionUID = 2396527384580755236L;

	public ForbiddenException(String format, Object... formatArgs) {
		super(HttpServletResponse.SC_FORBIDDEN, format, formatArgs);
	}

	public ForbiddenException(Throwable cause, String format, Object... formatArgs) {
		super(cause, HttpServletResponse.SC_FORBIDDEN, format, formatArgs);
	}
}
