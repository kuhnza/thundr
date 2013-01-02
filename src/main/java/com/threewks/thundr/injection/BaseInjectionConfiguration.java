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

import com.threewks.thundr.action.method.ActionInterceptorRegistry;
import com.threewks.thundr.view.ViewResolverRegistry;

public class BaseInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		addServices(injectionContext);

		ActionInterceptorRegistry actionInterceptorRegistry = injectionContext.get(ActionInterceptorRegistry.class);
		addActionInterceptors(actionInterceptorRegistry);

		ViewResolverRegistry viewResolverRegistry = injectionContext.get(ViewResolverRegistry.class);
		addViewResolvers(viewResolverRegistry, injectionContext);

	}

	protected void addServices(UpdatableInjectionContext injectionContext) {
	}

	protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
	}

	protected void addActionInterceptors(ActionInterceptorRegistry actionInterceptorRegistry) {
	}
}
