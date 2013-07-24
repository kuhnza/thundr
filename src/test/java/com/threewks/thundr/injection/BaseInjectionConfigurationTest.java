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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.threewks.thundr.action.method.ActionInterceptorRegistry;
import com.threewks.thundr.view.ViewResolverRegistry;

public class BaseInjectionConfigurationTest {
	private ActionInterceptorRegistry actionInterceptorRegistry = null;
	private UpdatableInjectionContext updatableInjectionContext = null;
	private ViewResolverRegistry viewResolverRegistry = null;

	@Test
	public void shouldProvideAndInvokeStandardExtensionPoints() {
		BaseInjectionConfiguration injectionConfig = new BaseInjectionConfiguration() {
			@Override
			protected void addActionInterceptors(ActionInterceptorRegistry actionInterceptorRegistry) {
				BaseInjectionConfigurationTest.this.actionInterceptorRegistry = actionInterceptorRegistry;
			}

			@Override
			protected void addServices(UpdatableInjectionContext injectionContext) {
				BaseInjectionConfigurationTest.this.updatableInjectionContext = injectionContext;
			}

			@Override
			protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
				BaseInjectionConfigurationTest.this.viewResolverRegistry = viewResolverRegistry;
			}
		};
		InjectionContextImpl injectionContext = new InjectionContextImpl();
		ActionInterceptorRegistry mockActionInterceptorRegistry = mock(ActionInterceptorRegistry.class);
		ViewResolverRegistry mockViewResolverRegistry = mock(ViewResolverRegistry.class);
		injectionContext.inject(mockActionInterceptorRegistry).as(ActionInterceptorRegistry.class);
		injectionContext.inject(mockViewResolverRegistry).as(ViewResolverRegistry.class);

		injectionConfig.configure(injectionContext);

		assertThat(actionInterceptorRegistry, is(mockActionInterceptorRegistry));
		assertThat(updatableInjectionContext, is((UpdatableInjectionContext) injectionContext));
		assertThat(viewResolverRegistry, is(mockViewResolverRegistry));

	}
}
