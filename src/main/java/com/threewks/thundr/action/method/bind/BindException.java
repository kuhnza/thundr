package com.threewks.thundr.action.method.bind;

import com.threewks.thundr.exception.BaseException;

public class BindException extends BaseException {
	private static final long serialVersionUID = -4369379121038517032L;

	public BindException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public BindException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}
