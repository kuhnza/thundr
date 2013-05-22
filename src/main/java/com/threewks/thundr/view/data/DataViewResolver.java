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
package com.threewks.thundr.view.data;

import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.logger.Logger;
import com.threewks.thundr.view.*;
import jodd.util.MimeTypes;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class DataViewResolver implements ViewResolver<DataView> {
	private static final String ContentTypeAny = "*/*";

	private ViewResolverRegistry registry;
	private String defaultContentType;
	private Map<String, Constructor> strategies = new HashMap<String, Constructor>();

	public DataViewResolver(ViewResolverRegistry registry) {
		this(registry, MimeTypes.MIME_APPLICATION_JSON);
	}

	public DataViewResolver(ViewResolverRegistry registry, String defaultResponseContentType) {
		this.registry = registry;
		this.defaultContentType = defaultResponseContentType;
	}

	public void addDataViewType(String contentType, Class<? extends DataView> viewClass) {
		// Resolve constructor and add it to available view strategies.
		try {
			Constructor c = viewClass.getConstructor(Object.class, int.class);
			strategies.put(contentType, c);
		} catch (NoSuchMethodException e) {
			// TODO make typed exception
			throw new RuntimeException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void resolve(HttpServletRequest req, HttpServletResponse resp, DataView intermediateView) {
		String responseContentType = determineResponseContentType(req);
		Constructor viewConstructor = strategies.get(responseContentType);
		if (viewConstructor == null) {
			// Fine to throw exception here to be handled by container since we don't have any idea about what
			// an acceptable format might be. Everything else ought to be handled by the appropriate delegate.
			throw new HttpStatusException(HttpSupport.Status.NotAcceptable, "Not acceptable: %s", responseContentType);
		}

		View view = constructView(viewConstructor, intermediateView.getOutput(), intermediateView.getStatus());

		ViewResolver resolver =  registry.findViewResolver(view);
		if (resolver == null) {
			throw new HttpStatusException(HttpSupport.Status.InternalServerError, "No view resolver found for: %s", view.getClass());
		}

		try {
			resolver.resolve(req, resp, view);
		} catch (HttpStatusException hse) {
			Logger.error(hse.getMessage() + "\n" + StringUtils.join(hse.getStackTrace(), '\n'));
			view = constructView(viewConstructor, hse, hse.getStatus());
			resolver.resolve(req, resp, view);
		} catch (Exception e) {
			Logger.error(e.getMessage() + "\n" + StringUtils.join(e.getStackTrace(), '\n'));
			view = constructView(viewConstructor, e, HttpSupport.Status.InternalServerError);
			resolver.resolve(req, resp, view);
		}
	}

	private String determineResponseContentType(HttpServletRequest req) {
		// Check for presence of an explicit format parameter
		String format = req.getParameter("format");
		if (format != null) {
			String contentType = MimeTypes.lookupMimeType(format);
			if (contentType == null) {
				return format;
			} else {
				return contentType;
			}
		}

		// No dice, check for Accept header. Here we accept the first matching
		// header element or a wildcard in which case we fall through to the default.
		String accept = req.getHeader("Accept");
		if (accept != null) {
			for (String contentType : StringUtils.split(accept, ',')) {
				if (contentType.contains(";")) {
					contentType = contentType.replaceAll(";.*", "");
				}

				if (ContentTypeAny.equals(contentType)) {
					break;
				} else if (strategies.containsKey(contentType)) {
					return contentType;
				}
			}
		}

		// No format specified so lets go with the default
		return defaultContentType;
	}

	private View constructView(Constructor viewConstructor, Object output, int status) {
		try {
			return (View) viewConstructor.newInstance(output, status);
		} catch (Exception e) {
			throw new HttpStatusException(e, HttpSupport.Status.InternalServerError, "An error occurred attempting to instantiate view: %s", e.getMessage());
		}
	}
}
