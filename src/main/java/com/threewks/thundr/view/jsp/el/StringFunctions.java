package com.threewks.thundr.view.jsp.el;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class StringFunctions {
	public static String upperCase(Object arg) {
		return arg == null ? null : StringUtils.upperCase(arg.toString());
	}

	public static String lowerCase(Object arg) {
		return arg == null ? null : StringUtils.lowerCase(arg.toString());
	}

	public static String sentenceCase(Object arg) {
		return arg == null ? null : StringUtils.capitalize(arg.toString());
	}

	public static String capitalise(Object arg) {
		return arg == null ? null : WordUtils.capitalizeFully(arg.toString());
	}
	
	public static String replace(Object arg, String regex, String replacement) {
		return arg == null ? "" : arg.toString().replaceAll(regex == null ? "" : regex, replacement == null ? "" : replacement);
	}
}
