package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.List;

public enum RouteType {
	GET, POST, PUT, DELETE, HEAD;

	private static final List<RouteType> all = list(GET, POST, PUT, DELETE);

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
