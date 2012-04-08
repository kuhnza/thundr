package com.atomicleopard.webFramework;

import com.atomicleopard.webFramework.routes.Routes;
import com.google.inject.AbstractModule;

public class WebFrameworkModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Routes.class).to(Routes.class);
	}

}
