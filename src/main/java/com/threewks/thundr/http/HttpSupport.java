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
package com.threewks.thundr.http;

import java.net.URI;

public class HttpSupport {
	public static class Header {
		public static final String ContentLength = "Content-Length";
		public static final String ContentType = "Content-Type";
		public static final String ContentDisposition = "Content-Disposition";
		public static final String LastModified = "Last-Modified";
		public static final String Expires = "Expires";
		public static final String CacheControl = "Cache-Control";
		public static final String AcceptEncoding = "Accept-Encoding";
		public static final String ContentEncoding = "Content-Encoding";
		public static final String SetCookie = "Set-Cookie";
		public static final String SetCookie2 = "Set-Cookie2";
		public static final String Pragma = "Pragma";
	}

	public static String convertToValidUrl(String url) {
		try {
			return new URI(url).toASCIIString();
		} catch (Exception e) {
			return url;
		}
	}
}
