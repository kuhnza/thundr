package com.threewks.thundr.route;

import com.threewks.thundr.http.exception.NotFoundException;

public class RouteNotFoundException extends NotFoundException {
	private static final long serialVersionUID = -5593794422027970405L;

	public RouteNotFoundException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public RouteNotFoundException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}
}
