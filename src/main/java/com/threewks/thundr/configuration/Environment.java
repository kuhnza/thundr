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
package com.threewks.thundr.configuration;

public class Environment {
	/**
	 * 'dev' is the conventional local development environment for thundr apps.
	 * i.e. when running on localhost.
	 */
	public static final String DEV = "dev";

	private static String environment = null;

	/**
	 * Gets the current environment name.
	 * 
	 * @return
	 */
	public static String get() {
		return environment;
	}

	/**
	 * Sets the current environment name. thundr expects this value to
	 * be well established at application start up time, and not to change
	 * during application running. This method is primarily available for test code,
	 * but is also used to specify the environment at startup.
	 * 
	 * @param environment
	 */
	public static void set(String environment) {
		Environment.environment = environment;
	}

	/**
	 * Returns true if the current environment matches the given value
	 * 
	 * @param environment
	 * @return
	 */
	public static boolean is(String environment) {
		return Environment.environment == null ? environment == null : Environment.environment.equals(environment);
	}

}
