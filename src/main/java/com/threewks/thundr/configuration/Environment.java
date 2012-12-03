package com.threewks.thundr.configuration;

public class Environment {
	private static String environment;

	public static String get() {
		return environment;
	}

	public static void set(String environment) {
		Environment.environment = environment;
	}
}
