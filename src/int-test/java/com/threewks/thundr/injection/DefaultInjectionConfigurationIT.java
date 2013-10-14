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
package com.threewks.thundr.injection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.threewks.thundr.module.Modules;
import com.threewks.thundr.route.RouteType;
import com.threewks.thundr.route.Routes;
import com.threewks.thundr.view.ViewResolverRegistry;
import com.threewks.thundr.view.string.StringView;

public class DefaultInjectionConfigurationIT {
	private DefaultInjectionConfiguration config = new DefaultInjectionConfiguration();
	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();

	@Test
	public void shouldInjectionModulesAndRoutesIntoInjectionContext() {
		config.configure(injectionContext);

		assertThat(injectionContext.get(Modules.class), is(notNullValue()));
		assertThat(injectionContext.get(Routes.class), is(notNullValue()));
	}

	@Test
	public void shouldHaveRunConfigurationInjectionConfiguration() {
		config.configure(injectionContext);

		assertThat(injectionContext.get(String.class, "expectedValue"), is("present"));
	}

	@Test
	public void shouldHaveRunRouteInjectionConfiguration() {
		config.configure(injectionContext);
		Routes routes = injectionContext.get(Routes.class);
		assertThat(routes.findMatchingRoute("/something", RouteType.GET), is(notNullValue()));
	}

	@Test
	public void shouldHaveRunViewResolverInjectionConfiguration() {
		config.configure(injectionContext);
		ViewResolverRegistry registry = injectionContext.get(ViewResolverRegistry.class);

		assertThat(registry, is(notNullValue()));
		assertThat(registry.findViewResolver(new StringView("")), is(notNullValue()));
	}
}
