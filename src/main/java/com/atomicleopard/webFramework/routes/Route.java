package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atomicleopard.expressive.EList;

public class Route {
	private static final Pattern PathParameterToken = Pattern.compile("\\{(.*?)\\}");
	static final String AcceptablePathCharacters = "\\w%\\.\\-";
	static final String AcceptableMultiPathCharacters = AcceptablePathCharacters + "/";

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
			results.put(name, matcher.group(i));
		}
		return results;
	}

	@Override
	public String toString() {
		return String.format("%s %s\t->\t%s", routeType, route, actionName);
	}

	static String convertPathStringToRegex(String route) {
		route = PathParameterToken.matcher(route).replaceAll(Matcher.quoteReplacement("([" + AcceptablePathCharacters + "]+)"));
		route = route.replaceAll("\\*", Matcher.quoteReplacement("[" + AcceptablePathCharacters + "]*?"));
		// for double asterisk, replace the above replacement
		String acceptablePathCharsEscaped = "\\[\\\\w%\\\\.\\\\-\\]\\*\\?";
		route = route.replaceAll(acceptablePathCharsEscaped + acceptablePathCharsEscaped, Matcher.quoteReplacement("[" + AcceptableMultiPathCharacters + "]*?"));
		return route;
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
