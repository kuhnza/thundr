package com.threewks.thundr.action;

import javax.servlet.ServletContext;

import com.threewks.thundr.action.method.ActionInterceptorRegistry;
import com.threewks.thundr.action.method.MethodAction;
import com.threewks.thundr.action.method.MethodActionResolver;
import com.threewks.thundr.action.redirect.RedirectAction;
import com.threewks.thundr.action.redirect.RedirectActionResolver;
import com.threewks.thundr.action.rewrite.RewriteAction;
import com.threewks.thundr.action.rewrite.RewriteActionResolver;
import com.threewks.thundr.action.staticResource.StaticResourceAction;
import com.threewks.thundr.action.staticResource.StaticResourceActionResolver;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.route.Routes;

public class ActionInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Routes routes = injectionContext.get(Routes.class);
		ServletContext servletContext = injectionContext.get(ServletContext.class);

		MethodActionResolver methodActionResolver = new MethodActionResolver(injectionContext);
		injectionContext.inject(MethodActionResolver.class).as(methodActionResolver);
		// The MethodActionResolver is special because we use it to perform controller interception
		injectionContext.inject(ActionInterceptorRegistry.class).as(methodActionResolver);

		routes.addActionResolver(RedirectAction.class, new RedirectActionResolver());
		routes.addActionResolver(RewriteAction.class, new RewriteActionResolver(routes));
		routes.addActionResolver(StaticResourceAction.class, new StaticResourceActionResolver(servletContext));
		routes.addActionResolver(MethodAction.class, methodActionResolver);
	}
}
