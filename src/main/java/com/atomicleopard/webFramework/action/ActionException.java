package com.atomicleopard.webFramework.action;

import com.atomicleopard.webFramework.exception.BaseException;

public class ActionException extends BaseException {
	private static final long serialVersionUID = -9066873416042050258L;

	public ActionException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public ActionException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}
