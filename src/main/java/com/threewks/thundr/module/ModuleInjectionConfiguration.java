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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.configuration.PropertiesLoader;
import com.threewks.thundr.injection.BaseInjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;

public class ModuleInjectionConfiguration extends BaseInjectionConfiguration {
	public static final String FilenameModulesProperties = "modules.properties";
	public static final String PropertyNameModules = "thundrModules";

	private PropertiesLoader propertiesLoader = new PropertiesLoader();
	private String filename = FilenameModulesProperties;
	private String propertyName = PropertyNameModules;

	public ModuleInjectionConfiguration() {
	}

	@Override
	public void initialise(UpdatableInjectionContext injectionContext) {
		Modules modules = injectionContext.get(Modules.class);
		List<String> moduleNames = loadModules(injectionContext);
		for (String moduleName : moduleNames) {
			modules.addModule(moduleName);
		}
	}

	protected List<String> loadModules(UpdatableInjectionContext injectionContext) {
		List<String> moduleNames = new ArrayList<String>();
		List<String> modulesFromInjectionContext = attemptToLoadModuleFromInjectionContext(injectionContext);
		List<String> modulesFromPropertiesFile = attemptToLoadModulesFromModulesProperties();
		moduleNames.addAll(modulesFromInjectionContext);
		moduleNames.addAll(modulesFromPropertiesFile);

		if (moduleNames.isEmpty()) {
			throw new ModuleLoadingException("", "No modules have been specified - you must minimally specify your application module in either your application configuration property '%s' or the file '%s'", propertyName, filename);
		}
		return moduleNames;
	}

	@SuppressWarnings("unchecked")
	private List<String> attemptToLoadModuleFromInjectionContext(UpdatableInjectionContext injectionContext) {
		List<String> results = new ArrayList<String>();
		if (injectionContext.contains(String.class, propertyName)) {
			String stringProperty = injectionContext.get(String.class, propertyName);
			results.addAll(Arrays.asList(stringProperty.split("[;:,]")));
		}
		if (injectionContext.contains(List.class, propertyName)) {
			results.addAll(injectionContext.get(List.class, propertyName));
		}
		return results;
	}

	private List<String> attemptToLoadModulesFromModulesProperties() {
		Map<String, String> properties = propertiesLoader.loadSafe(filename);
		return properties == null ? Collections.<String> emptyList() : Expressive.list(properties.keySet());
	}
}
