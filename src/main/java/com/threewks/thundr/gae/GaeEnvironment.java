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
