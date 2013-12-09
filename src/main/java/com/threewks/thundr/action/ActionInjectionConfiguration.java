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
package com.threewks.thundr.action;

import javax.servlet.ServletContext;

import com.threewks.thundr.action.method.ActionInterceptorRegistry;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.action.method.MethodActionResolver;
import com.threewks.thundr.action.method.bind.ActionMethodBinderRegistry;
import com.threewks.thundr.action.redirect.RedirectAction;
import com.threewks.thundr.action.redirect.RedirectActionResolver;
import com.threewks.thundr.action.rewrite.RewriteAction;
import com.threewks.thundr.action.rewrite.RewriteActionResolver;
import com.threewks.thundr.action.staticResource.StaticResourceAction;
import com.threewks.thundr.action.staticResource.StaticResourceActionResolver;
import com.threewks.thundr.injection.BaseInjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.route.Routes;

public class ActionInjectionConfiguration extends BaseInjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Routes routes = injectionContext.get(Routes.class);
		ServletContext servletContext = injectionContext.get(ServletContext.class);

		MethodActionResolver methodActionResolver = new MethodActionResolver(injectionContext);
		injectionContext.inject(methodActionResolver).as(MethodActionResolver.class);
		// The MethodActionResolver is special because we use it to perform controller interception
		injectionContext.inject(methodActionResolver).as(ActionInterceptorRegistry.class);
		injectionContext.inject(methodActionResolver.getMethodBinderRegistry()).as(ActionMethodBinderRegistry.class);

		routes.addActionResolver(RedirectAction.class, new RedirectActionResolver());
		routes.addActionResolver(RewriteAction.class, new RewriteActionResolver(routes));
		routes.addActionResolver(StaticResourceAction.class, new StaticResourceActionResolver(servletContext));
		routes.addActionResolver(MethodAction.class, methodActionResolver);
	}
}
