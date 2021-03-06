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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	
	public static List<String> split(Object arg, String regex) {
		return arg == null ? Collections.<String>emptyList() : Arrays.asList(arg.toString().split(regex == null ? "\\s" : regex));
	}
}
