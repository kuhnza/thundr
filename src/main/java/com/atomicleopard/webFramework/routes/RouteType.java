package com.atomicleopard.webFramework.routes;

import java.util.List;

import scala.actors.threadpool.Arrays;

public enum RouteType {
	GET, POST, PUT, DELETE, HEAD;

	@SuppressWarnings("unchecked")
	private static final List<RouteType> all = Arrays.asList(RouteType.values());

	public static List<RouteType> all() {
		return all;
	}

	public static RouteType from(String string) {
		for (RouteType type : values()) {
			if (type.name().equalsIgnoreCase(string)) {
				return type;
			}
		}
		return null;
	}
}
