package com.atomicleopard.webFramework.action;

import javax.servlet.ServletContext;

import com.atomicleopard.webFramework.action.method.ActionInterceptorRegistry;
import com.atomicleopard.webFramework.action.method.MethodAction;
import com.atomicleopard.webFramework.action.method.MethodActionResolver;
import com.atomicleopard.webFramework.action.redirect.RedirectAction;
import com.atomicleopard.webFramework.action.redirect.RedirectActionResolver;
import com.atomicleopard.webFramework.action.rewrite.RewriteAction;
import com.atomicleopard.webFramework.action.rewrite.RewriteActionResolver;
import com.atomicleopard.webFramework.action.staticResource.StaticResourceAction;
import com.atomicleopard.webFramework.action.staticResource.StaticResourceActionResolver;
import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.route.Routes;

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
