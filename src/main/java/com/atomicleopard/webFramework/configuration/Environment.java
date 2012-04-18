package com.atomicleopard.webFramework.configuration;

public class Environment {
	public static Environment Production = new Environment("Production");
	public static Environment Development = new Environment("Development");

	private String environment;

	public Environment(String environment) {
		super();
		this.environment = environment;
	}

	public String getEnvironment() {
		return environment;
	}
}
