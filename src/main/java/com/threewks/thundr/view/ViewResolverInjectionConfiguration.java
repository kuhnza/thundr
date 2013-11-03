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
package com.threewks.thundr.view;

import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.route.RouteNotFoundException;
import com.threewks.thundr.view.exception.ExceptionViewResolver;
import com.threewks.thundr.view.exception.HttpStatusExceptionViewResolver;
import com.threewks.thundr.view.exception.RouteNotFoundViewResolver;
import com.threewks.thundr.view.file.FileView;
import com.threewks.thundr.view.file.FileViewResolver;
import com.threewks.thundr.view.json.JsonView;
import com.threewks.thundr.view.json.JsonViewResolver;
import com.threewks.thundr.view.jsonp.JsonpView;
import com.threewks.thundr.view.jsonp.JsonpViewResolver;
import com.threewks.thundr.view.jsp.JspView;
import com.threewks.thundr.view.jsp.JspViewResolver;
import com.threewks.thundr.view.redirect.RedirectView;
import com.threewks.thundr.view.redirect.RedirectViewResolver;
import com.threewks.thundr.view.string.StringView;
import com.threewks.thundr.view.string.StringViewResolver;

public class ViewResolverInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
		injectionContext.inject(viewResolverRegistry).as(ViewResolverRegistry.class);
		addViewResolvers(viewResolverRegistry, injectionContext);
	}

	protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
		ExceptionViewResolver exceptionViewResolver = new ExceptionViewResolver();
		HttpStatusExceptionViewResolver statusViewResolver = new HttpStatusExceptionViewResolver();

		injectionContext.inject(exceptionViewResolver).as(ExceptionViewResolver.class);
		injectionContext.inject(statusViewResolver).as(HttpStatusExceptionViewResolver.class);

		viewResolverRegistry.addResolver(Throwable.class, exceptionViewResolver);
		viewResolverRegistry.addResolver(HttpStatusException.class, statusViewResolver);
		viewResolverRegistry.addResolver(RouteNotFoundException.class, new RouteNotFoundViewResolver());
		viewResolverRegistry.addResolver(RedirectView.class, new RedirectViewResolver());
		viewResolverRegistry.addResolver(JsonView.class, new JsonViewResolver());
		viewResolverRegistry.addResolver(JsonpView.class, new JsonpViewResolver());
		viewResolverRegistry.addResolver(FileView.class, new FileViewResolver());
		viewResolverRegistry.addResolver(JspView.class, new JspViewResolver());
		viewResolverRegistry.addResolver(StringView.class, new StringViewResolver());
	}
}
