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

import com.threewks.thundr.action.ActionInjectionConfiguration;
import com.threewks.thundr.configuration.ConfigurationInjectionConfiguration;
import com.threewks.thundr.module.Module;
import com.threewks.thundr.module.ModuleInjectionConfiguration;
import com.threewks.thundr.module.Modules;
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
		modules.addModule(Module.from(new ActionInjectionConfiguration()));
		modules.addModule(Module.from(new ViewResolverInjectionConfiguration()));
		modules.addModule(Module.from(new ModuleInjectionConfiguration()));
		modules.loadModules(injectionContext);

		modules.addModule(Module.from(new RouteInjectionConfiguration()));
		modules.loadModules(injectionContext);
	}
}
