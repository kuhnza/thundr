package com.atomicleopard.webFramework.route;

import com.atomicleopard.webFramework.http.exception.NotFoundException;

public class RouteNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5593794422027970405L;

	public RouteNotFoundException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public RouteNotFoundException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}
}
