package com.atomicleopard.webFramework.view;

import com.atomicleopard.webFramework.exception.BaseException;

public class ViewResolutionException extends BaseException {
	private static final long serialVersionUID = 5541928967880619784L;

	public ViewResolutionException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public ViewResolutionException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}
