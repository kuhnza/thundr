package com.threewks.thundr.injection;

import com.threewks.thundr.action.ActionInjectionConfiguration;
import com.threewks.thundr.configuration.ConfigurationInjectionConfiguration;
import com.threewks.thundr.module.Module;
import com.threewks.thundr.module.ModuleInjectionConfiguration;
import com.threewks.thundr.module.Modules;
import com.threewks.thundr.profiler.ProfilerInjectionConfiguration;
import com.threewks.thundr.route.RouteInjectionConfiguration;
import com.threewks.thundr.route.Routes;
import com.threewks.thundr.view.ViewResolverInjectionConfiguration;

public class DefaultInjectionConfiguration implements InjectionConfiguration {
	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Modules modules = new Modules();
		Routes routes = new Routes();
		injectionContext.inject(modules).as(Modules.class);
		injectionContext.inject(routes).as(Routes.class);

		modules.addModule(Module.from(new ConfigurationInjectionConfiguration()));
		modules.addModule(Module.from(new ProfilerInjectionConfiguration()));
		modules.addModule(Module.from(new ActionInjectionConfiguration()));
		modules.addModule(Module.from(new ViewResolverInjectionConfiguration()));
		modules.addModule(Module.from(new ModuleInjectionConfiguration()));
		modules.loadModules(injectionContext);

		modules.addModule(Module.from(new RouteInjectionConfiguration()));
		modules.loadModules(injectionContext);
	}
}
