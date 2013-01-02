/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
