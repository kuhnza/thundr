package com.threewks.thundr.gae;

import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.utils.SystemProperty.Environment;

public class GaeEnvironment {
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

	public static boolean isProduction() {
		return Environment.Value.Production.name() == SystemProperty.Environment.environment.get();
	}
}
