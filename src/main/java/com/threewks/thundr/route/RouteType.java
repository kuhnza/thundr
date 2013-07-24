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
package com.threewks.thundr.route;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public enum RouteType {
	GET,
	POST,
	PUT,
	PATCH,
	DELETE;

	private static final List<RouteType> all = list(RouteType.values());
	private static final Map<String, RouteType> lookup = createLookup();

	public static List<RouteType> all() {
		return all;
	}

	private static Map<String, RouteType> createLookup() {
		Map<String, RouteType> map = new HashMap<String, RouteType>();
		for (RouteType routeType : all) {
			map.put(routeType.name(), routeType);
		}

		return map;
	}

	public static RouteType from(String routeType) {
		return lookup.get(StringUtils.trimToEmpty(StringUtils.upperCase(routeType)));
	}
}
