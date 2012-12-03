package com.threewks.thundr.http.service.gae;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.threewks.thundr.http.service.HttpService;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class ServiceInjectionConfiguration implements com.threewks.thundr.injection.InjectionConfiguration {
	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
		injectionContext.inject(urlFetchService).as(URLFetchService.class);
		injectionContext.inject(HttpServiceImpl.class).as(HttpService.class);
		Logger.info("Loaded HttpService module");
	}
}
