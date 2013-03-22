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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import com.threewks.thundr.exception.BaseException;

/**
 * Encodes URL elements as needed.
 * 
 * see http://tools.ietf.org/html/rfc2396 for details.
 */
public class URLEncoder {
	private static final String unreserved = "a-zA-Z0-9";
	private static final String mark = "_!~*'()\\.\\-";
	private static final Pattern acceptableQueryCharacters = Pattern.compile("[" + unreserved + mark + "]*");
	private static final Pattern acceptablePathCharacters = Pattern.compile("[" + unreserved + mark + "]*");

	/**
	 * Encodes the given value to be used as a URL/URI path component.
	 */
	public static final String encodePathComponent(String value) {
		return escapeUsingPattern(acceptablePathCharacters, value);
	}

	/**
	 * Encodes the given value to be used as a URL/URI path slug - that is it removes non-alpha characters and introduces dashes.
	 * 
	 * @param value
	 * @return
	 */
	public static final String encodePathSlugComponent(String value) {
		return value == null ? value : value.replaceAll("'", "").replaceAll("\\W", "-").replaceAll("-+", "-");
	}

	/**
	 * Encodes the given value to be used as a URL/URI query component.
	 */
	public static final String encodeQueryComponent(String value) {
		return escapeUsingPattern(acceptableQueryCharacters, value);
	}

	public static final String decodePathComponent(String value) {
		return unescape(value);
	}

	private static String unescape(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e);
		}
	}

	private static String escapeUsingPattern(Pattern pattern, String value) {
		if (value == null || value.length() == 0 || pattern.matcher(value).matches()) {
			return value;
		}
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			String character = value.substring(i, i + 1);

			if (!pattern.matcher(character).matches()) {
				output.append("%");
				output.append(Integer.toHexString((int) character.charAt(0)).toUpperCase());
			} else {
				output.append(character);
			}
		}
		return output.toString();
	}
}