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
package com.threewks.thundr.view;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.HttpSupport;
import jodd.util.StringPool;

import javax.servlet.http.HttpServletResponse;

public class ViewOptions {

	public static ViewOptions Default = new ViewOptions();

	private String characterEncoding = StringPool.UTF_8;
	private String contentType = ContentType.TextHtml.value();
	private int status = HttpSupport.Status.OK;

	private ViewOptions() {}

	public ViewOptions withCharacterEncoding(String characterEncoding) {
		ViewOptions copy = copy() ;
		copy.characterEncoding = characterEncoding;
		return copy;
	}

	public ViewOptions withContentType(String contentType) {
		ViewOptions copy = copy() ;
		copy.contentType = contentType;
		return copy;
	}

	public ViewOptions withStatus(int status) {
		ViewOptions copy = copy() ;
		copy.status = status;
		return copy;
	}

	public void apply(HttpServletResponse resp) {
		resp.setCharacterEncoding(this.characterEncoding);
		resp.setContentType(this.contentType);
		resp.setStatus(this.status);
	}

	ViewOptions copy() {
		ViewOptions copy = new ViewOptions();
		copy.characterEncoding = this.characterEncoding;
		copy.contentType = this.contentType;
		copy.status = this.status;
		return copy;
	}
}
