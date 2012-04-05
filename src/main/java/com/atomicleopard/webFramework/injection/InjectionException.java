package com.atomicleopard.webFramework.injection;

import com.atomicleopard.webFramework.exception.BaseException;

public class InjectionException extends BaseException {
	private static final long serialVersionUID = -3265743532426086124L;

	public InjectionException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public InjectionException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}
}
