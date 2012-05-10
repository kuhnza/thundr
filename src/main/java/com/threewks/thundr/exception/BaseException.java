package com.threewks.thundr.exception;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 2489531688803100876L;

	public BaseException() {
		super();
	}

	public BaseException(Throwable cause, String format, Object... formatArgs) {
		super(String.format(format, formatArgs), cause);
	}

	public BaseException(String format, Object... formatArgs) {
		super(String.format(format, formatArgs));
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

}
