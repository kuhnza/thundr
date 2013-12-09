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
package com.threewks.thundr.view.jsp.el;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.http.URLEncoder;
import com.threewks.thundr.route.Route;
import com.threewks.thundr.route.Routes;

public class UrlFunctions {
	/**
	 * URL encodes the given path so that it is correctly represented for requests.
	 * This encoding applies to path components, ie. /the/values/in/the/path/string
	 * 
	 * To encode request parameters, see {@link #param(String)} or {@link URLEncoder}
	 * 
	 * @param path
	 * @return
	 * @see #param(String)
	 * @see URLEncoder
	 */
	public static String path(String path) {
		return URLEncoder.encodePathComponent(path);
	}

	/**
	 * Converts the given path component to a 'slug' representation, that is the text is normalised to be conventiently human readable.
	 * This is a one way transformation.
	 * 
	 * @param path
	 * @return
	 */
	public static String pathSlug(String path) {
		return URLEncoder.encodePathSlugComponent(path);
	}

	/**
	 * URL encodes the given parameter so that it is correctly represented for requests.
	 * This encoding applies to query parameters components, ie. /?the=values&in=the&query
	 * 
	 * To encode path elements, see {@link #path(String)} or {@link URLEncoder}
	 * 
	 * @param param
	 * @return
	 * @see #path(String)
	 * @see URLEncoder
	 */
	public static String param(String param) {
		return URLEncoder.encodeQueryComponent(param);
	}

	/**
	 * Outputs a url for the given named route.
	 */
	public static String route(Routes routes, String routeName, Object...params) {
		Route route = routes.getRoute(routeName);
		return route.getReverseRoute(params);
	}
}
