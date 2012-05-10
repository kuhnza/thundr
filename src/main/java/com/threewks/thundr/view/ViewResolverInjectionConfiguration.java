package com.threewks.thundr.view;

import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.route.RouteNotFoundException;
import com.threewks.thundr.view.exception.ExceptionViewResolver;
import com.threewks.thundr.view.exception.HttpStatusExceptionViewResolver;
import com.threewks.thundr.view.exception.RouteNotFoundViewResolver;
import com.threewks.thundr.view.json.JsonView;
import com.threewks.thundr.view.json.JsonViewResolver;
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
		injectionContext.inject(ViewResolverRegistry.class).as(viewResolverRegistry);
		addViewResolvers(viewResolverRegistry, injectionContext);
	}

	protected void addViewResolvers(ViewResolverRegistry viewResolverRegistry, UpdatableInjectionContext injectionContext) {
		ExceptionViewResolver exceptionViewResolver = new ExceptionViewResolver();
		HttpStatusExceptionViewResolver statusViewResolver = new HttpStatusExceptionViewResolver();
		
		injectionContext.inject(ExceptionViewResolver.class).as(exceptionViewResolver);
		injectionContext.inject(HttpStatusExceptionViewResolver.class).as(statusViewResolver);
		
		viewResolverRegistry.addResolver(Throwable.class, exceptionViewResolver);
		viewResolverRegistry.addResolver(HttpStatusException.class, statusViewResolver);
		viewResolverRegistry.addResolver(RouteNotFoundException.class, new RouteNotFoundViewResolver());
		viewResolverRegistry.addResolver(RedirectView.class, new RedirectViewResolver());
		viewResolverRegistry.addResolver(JsonView.class, new JsonViewResolver());
		viewResolverRegistry.addResolver(JspView.class, new JspViewResolver());
		viewResolverRegistry.addResolver(StringView.class, new StringViewResolver());
	}
}
