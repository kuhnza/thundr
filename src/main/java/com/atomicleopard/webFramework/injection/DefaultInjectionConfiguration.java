package com.atomicleopard.webFramework.injection;

import com.atomicleopard.webFramework.action.ActionInjectionConfiguration;
import com.atomicleopard.webFramework.configuration.ConfigurationInjectionConfiguration;
import com.atomicleopard.webFramework.module.Module;
import com.atomicleopard.webFramework.module.ModuleInjectionConfiguration;
import com.atomicleopard.webFramework.module.Modules;
import com.atomicleopard.webFramework.route.RouteInjectionConfiguration;
import com.atomicleopard.webFramework.route.Routes;
import com.atomicleopard.webFramework.view.ViewResolverInjectionConfiguration;

public class DefaultInjectionConfiguration implements InjectionConfiguration {
	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Modules modules = new Modules();
		Routes routes = new Routes();
		injectionContext.inject(Modules.class).as(modules);
		injectionContext.inject(Routes.class).as(routes);

		modules.addModule(Module.from(new ConfigurationInjectionConfiguration()));
		modules.addModule(Module.from(new ActionInjectionConfiguration()));
		modules.addModule(Module.from(new ViewResolverInjectionConfiguration()));
		modules.addModule(Module.from(new ModuleInjectionConfiguration()));
		modules.loadModules(injectionContext);
		
		modules.addModule(Module.from(new RouteInjectionConfiguration()));
		modules.loadModules(injectionContext);
	}
}
