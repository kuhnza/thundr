package com.atomicleopard.webFramework.route;

import com.atomicleopard.webFramework.exception.BaseException;

public class RouteException extends BaseException {

	private static final long serialVersionUID = -5593794422027970405L;

	public RouteException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public RouteException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}
