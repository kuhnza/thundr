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
