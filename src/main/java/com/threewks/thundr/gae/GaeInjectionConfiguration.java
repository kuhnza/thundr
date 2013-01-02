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

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.threewks.thundr.configuration.Environment;
import com.threewks.thundr.http.service.HttpService;
import com.threewks.thundr.http.service.gae.HttpServiceImpl;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class GaeInjectionConfiguration implements InjectionConfiguration {
	private static final String DEV_APPLICATION_ID = "dev";

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		String applicationId = getApplicationId();
		Environment.set(applicationId);
		Logger.info("Running as environment %s", Environment.get());

		URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
		injectionContext.inject(urlFetchService).as(URLFetchService.class);
		injectionContext.inject(HttpServiceImpl.class).as(HttpService.class);
	}

	/**
	 * Get the application id of the running application. When running in Development mode
	 * (ie: in a local SDK environment) this simply returns the value "dev". When running
	 * in Production mode, this returns the value of the application id.
	 * 
	 * @return the application id.
	 */
	protected String getApplicationId() {
		String environment = SystemProperty.environment.get();
		if (SystemProperty.Environment.Value.Development.value().equals(environment)) {
			return DEV_APPLICATION_ID;
		} else {
			return SystemProperty.applicationId.get();
		}
	}
}