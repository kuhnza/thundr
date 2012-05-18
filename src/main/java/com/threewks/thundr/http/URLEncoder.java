package com.threewks.thundr.http;

import java.util.regex.Pattern;

/**
 * Encodes URL elements as needed.
 * 
 * see http://tools.ietf.org/html/rfc2396 for details.
 */
public class URLEncoder {
	private static final String unreserved = "a-zA-Z0-9";
	private static final String mark = "_!~*'()\\.\\-";
	private static final String pathChars = ":@&=+$,";
	private static final Pattern acceptableQueryCharacters = Pattern.compile("[" + unreserved + mark + "]*");
	private static final Pattern acceptablePathCharacters = Pattern.compile("[" + unreserved + mark + pathChars + "]*");

	/**
	 * Encodes the given value to be used as a URL/URI path component.
	 */
	public static final String encodePathComponent(String value) {
		return escapeUsingPattern(acceptablePathCharacters, value);
	}

	/**
	 * Encodes the given value to be used as a URL/URI query component.
	 */
	public static final String encodeQueryComponent(String value) {
		return escapeUsingPattern(acceptableQueryCharacters, value);
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