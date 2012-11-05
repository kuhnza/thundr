package com.threewks.thundr.configuration;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.utils.SystemProperty;

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

	public static boolean isProduction() {
		return Production.getEnvironment() == SystemProperty.Environment.environment.get();
	}

	/**
	 * @return The application version with dot separator appended for
	 *         production environment otherwise returns empty string
	 */
	public static String applicationVersion() {
		// The version identifier for the current application version.
		// Result is of the form <major>.<minor> where <major> is the
		// version name supplied at deploy time
		// and <minor> is a timestamp value maintained by App Engine
		if (isProduction() && SystemProperty.applicationVersion.get().contains(".")) {
			return StringUtils.substringBefore(SystemProperty.applicationVersion.get(), ".").concat(".");
		}

		return "";
	}

}
