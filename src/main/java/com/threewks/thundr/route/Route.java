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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atomicleopard.expressive.EList;
import com.threewks.thundr.http.URLEncoder;

public class Route {
	public static final Pattern PathParameterToken = Pattern.compile("\\{(.*?)\\}");
	// from -> http://www.ietf.org/rfc/rfc1738.txt: "Thus, only alphanumerics, the special characters "$-_.+!*'()," ... may be used unencoded within a URL."
	static final String AcceptablePathCharacters = "\\w%:@&=+$,!~*'()\\.\\-";
	static final String AcceptableMultiPathCharacters = AcceptablePathCharacters + "/";

	// TODO - Binding path segment request parameters
	// The spec allows for request parameters to be encoded in the format /url/url2/url3;key=value;key2=value2
	// This seems to only happen in jetty and tomcat (some versions) when redirects are invoked, particularly if the server
	// hasn't confirmed that the client supports cookies. i.e. redirect -> /go/here;jsessionid=12345678
	// This is a hack implementation which satisfies most current web development needs, further reading on a fuller implementation here:
	// http://www.skorks.com/2010/05/what-every-developer-should-know-about-urls/
	static final String SemiColonDelimitedRequestParameters = "(?:;.*?)*";

	private String route;
	private Pattern routeMatchRegex;
	private String actionName;
	private RouteType routeType;
	private EList<String> pathParameters;

	public Route(String route, String actionName, RouteType routeType) {
		super();
		this.route = route;
		this.actionName = actionName;
		this.routeType = routeType;
		this.pathParameters = extractPathParametersFromRoute(route);
		this.routeMatchRegex = Pattern.compile(convertPathStringToRegex(route));
	}

	public String getRouteMatchRegex() {
		return routeMatchRegex.pattern();
	}

	public String getActionName() {
		return actionName;
	}

	public RouteType getRouteType() {
		return routeType;
	}

	public boolean matches(String routePath) {
		return routeMatchRegex.matcher(routePath).matches();
	}

	public Map<String, String> getPathVars(String routePath) {
		Matcher matcher = routeMatchRegex.matcher(routePath);
		matcher.find();
		int count = matcher.groupCount();
		Map<String, String> results = new HashMap<String, String>();
		for (int i = 1; i <= count; i++) {
			String name = pathParameters.get(i - 1);

			results.put(name, URLEncoder.decodePathComponent(matcher.group(i)));
		}
		return results;
	}

	@Override
	public String toString() {
		return String.format("%s\t%s\t->\t%s", routeType, route, actionName);
	}

	static String convertPathStringToRegex(String route) {
		String wildCardPlaceholder = "____placeholder____";
		route = route.replaceAll("\\*\\*", wildCardPlaceholder);
		route = route.replaceAll("\\*", Matcher.quoteReplacement("[" + AcceptablePathCharacters + "]*?"));
		route = PathParameterToken.matcher(route).replaceAll(Matcher.quoteReplacement("([" + AcceptablePathCharacters + "]+)"));
		route = route.replaceAll(wildCardPlaceholder, Matcher.quoteReplacement("[" + AcceptableMultiPathCharacters + "]*?"));
		return route + SemiColonDelimitedRequestParameters;
	}

	static EList<String> extractPathParametersFromRoute(String route) {
		Matcher matcher = PathParameterToken.matcher(route);
		EList<String> results = list();
		while (matcher.find()) {
			String parameter = matcher.group(1);
			results.add(parameter);
		}
		return results;
	}
}
