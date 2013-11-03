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
package com.threewks.thundr.view.jsonp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.MimeTypes;
import jodd.util.StringPool;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threewks.thundr.json.GsonSupport;
import com.threewks.thundr.view.ViewResolutionException;
import com.threewks.thundr.view.ViewResolver;

public class JsonpViewResolver implements ViewResolver<JsonpView> {
	private GsonBuilder gsonBuilder;

	public JsonpViewResolver() {
		this(GsonSupport.createBasicGsonBuilder());
	}

	public JsonpViewResolver(GsonBuilder gsonBuilder) {
		this.gsonBuilder = gsonBuilder;
	}

	/**
	 * Exposes the underlying gson builder, allowing modification of the properties controlling how json is serialized.
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return gsonBuilder;
	}

	@Override
	public void resolve(HttpServletRequest req, HttpServletResponse resp, JsonpView viewResult) {
		Object output = viewResult.getOutput();
		try {
			Gson create = gsonBuilder.create();
			String json = create.toJson(output);
			String jsonp = getCallback(req) + "(" + json + ")";
			resp.setContentType(MimeTypes.MIME_APPLICATION_JAVASCRIPT);
			resp.setCharacterEncoding(StringPool.UTF_8);
			resp.setContentLength(json.getBytes(StringPool.UTF_8).length);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(jsonp);
		} catch (Exception e) {
			throw new ViewResolutionException(e, "Failed to generate JSONP output for object '%s': %s", output.toString(), e.getMessage());
		}
	}

	protected String getCallback(HttpServletRequest req) {
		Object callback = req.getParameter("callback");
		String callbackStr = callback == null ? null : StringUtils.trimToNull(callback.toString());
		callbackStr = callbackStr == null ? "callback" : callbackStr;
		return callbackStr;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
