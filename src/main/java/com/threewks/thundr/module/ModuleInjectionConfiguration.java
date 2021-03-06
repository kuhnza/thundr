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
package com.threewks.thundr.module;

import java.util.Collections;
import java.util.Map;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.configuration.PropertiesLoader;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class ModuleInjectionConfiguration implements InjectionConfiguration {
	private PropertiesLoader propertiesLoader = new PropertiesLoader();
	private String filename;

	public ModuleInjectionConfiguration() {
		this("modules.properties");
	}

	public ModuleInjectionConfiguration(String filename) {
		this.filename = filename;
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Modules modules = injectionContext.get(Modules.class);
		loadModules(modules, injectionContext, filename);
	}

	protected void loadModules(Modules modules, UpdatableInjectionContext injectionContext, String filename) {
		Logger.info("Loading modules from %s", filename);
		Map<String, String> properties = propertiesLoader.load(filename);
		if (properties.isEmpty()) {
			throw new ModuleLoadingException("", "you must have an entry for your application configuration in %s", filename);
		}
		EList<String> reverseOrder = Expressive.list(properties.keySet());
		Collections.reverse(reverseOrder);
		for (String key : reverseOrder) {
			Module module = Module.createModule(key);
			modules.addModule(module);
		}
	}
}
