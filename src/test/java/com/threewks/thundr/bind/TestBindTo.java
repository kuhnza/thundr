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
package com.threewks.thundr.bind;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class TestBindTo {
	public Object methodNone() {
		return "none";
	}

	public Object methodSingleString(String argument1) {
		return argument1;
	}

	public Object methodDoubleString(String argument1, String argument2) {
		return argument1 + ":" + argument2;
	}

	public Object methodStringList(List<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringSet(Set<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringCollection(Collection<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringArray(String[] argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodMap(Map<String, List<String>> argument1) {
		return StringUtils.join(argument1, ":");
	}

	// This warning cannot be suppressed but extending string is specific to some tests 
	public <T extends String> Object methodGenericArray(T[] argument1) {
		return StringUtils.join(argument1, ":");
	}

	public JavaBean methodJavaBean(JavaBean argument1) {
		return argument1;
	}
	
	public DeepJavaBean methodDeepJavaBean(DeepJavaBean argument1) {
		return argument1;
	}
}
