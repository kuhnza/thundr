package com.threewks.thundr.mail;

import com.threewks.thundr.exception.BaseException;

public class MailException extends BaseException {
	private static final long serialVersionUID = -6371243694231415866L;

	public MailException(String format, Object... formatArgs) {
		super(format, formatArgs);
	}

	public MailException(Throwable cause, String format, Object... formatArgs) {
		super(cause, format, formatArgs);
	}

}
