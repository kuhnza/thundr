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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Some common ContentType definitions for MIME types.
 * For a world of fun, check out http://www.iana.org/assignments/media-types/index.html
 */
public enum ContentType {
	TextPlain("text/plain"),
	TextHtml("text/html"),
	TextCss("text/css"),
	TextCsv("text/csv"),
	TextJavascript("text/javascript"),
	ApplicationFormUrlEncoded("application/x-www-form-urlencoded"),
	ApplicationOctetStream("application/octet-stream"),
	ApplicationJson("application/json"),
	ApplicationXml("application/xml"),
	ApplicationXmlDtd("application/xml-dtd"),
	ApplicationSoapXml("application/soap+xml"),
	MultipartFormData("multipart/form-data"),
	ImageJpeg("image/jpeg"),
	ImageGif("image/gif"),
	ImagePng("image/png"),
	Null(null);

	private String value;

	private ContentType(String encoding) {
		this.value = encoding;
	}

	public String value() {
		return value;
	}

	/**
	 * Returns true if the given string content type matches the content type represented by this enum value.
	 * 
	 * @param contentType
	 * @return
	 */
	public boolean matches(String contentType) {
		return StringUtils.equalsIgnoreCase(this.value, cleanContentType(contentType));
	}

	private static final Pattern contentTypePattern = Pattern.compile("([\\w+-]+)/([\\w+-]+?)(\\s*;.*)?");

	/**
	 * Removes extraneous information from the content-type header, such as encoding etc.
	 * If this is not recognised as a content type, returns the input.
	 * 
	 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
	 * @param rawContentType
	 * @return
	 */
	public static String cleanContentType(String rawContentType) {
		if (rawContentType != null) {
			Matcher matcher = contentTypePattern.matcher(rawContentType);
			if (matcher.matches()) {
				return (matcher.group(1) + "/" + matcher.group(2)).toLowerCase();
			}
		}
		return rawContentType;
	}

	public static ContentType from(String rawContentType) {
		rawContentType = cleanContentType(rawContentType);
		for (ContentType contentType : values()) {
			if (contentType.matches(rawContentType)) {
				return contentType;
			}
		}
		return null;
	}

	public static boolean anyMatch(Iterable<ContentType> types, String contentType) {
		contentType = cleanContentType(contentType);
		for (ContentType supported : types) {
			if (StringUtils.equalsIgnoreCase(supported.value, contentType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return value;
	}
}
