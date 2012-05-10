package com.threewks.thundr.http.exception;

import com.threewks.thundr.exception.BaseException;

public class HttpStatusException extends BaseException {
	private static final long serialVersionUID = 8001486426456728315L;
	private int status;

	public HttpStatusException(int status, String format, Object... formatArgs) {
		super(format, formatArgs);
		this.status = status;
	}

	public HttpStatusException(Throwable cause, int status, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
