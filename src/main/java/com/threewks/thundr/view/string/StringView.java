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
package com.threewks.thundr.view.string;

import com.threewks.thundr.view.View;

public class StringView implements View {
	private CharSequence content;
	private String contentType = null;

	public StringView(String content) {
		this.content = content;
	}

	public StringView(String format, Object... args) {
		this.content = String.format(format, args);
	}

	public CharSequence content() {
		return content;
	}

	public String contentType() {
		return contentType;
	}

	public StringView contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String toString() {
		return content.toString();
	}
}
