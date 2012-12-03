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