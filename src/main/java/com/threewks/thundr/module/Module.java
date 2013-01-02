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

import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.Cast;
import com.threewks.thundr.injection.InjectionConfiguration;

public class Module {
	private InjectionConfiguration configuration;
	private String name;

	private Module(InjectionConfiguration configuration) {
		this.configuration = configuration;
		this.name = loadModuleName(configuration);
	}

	public InjectionConfiguration getConfiguration() {
		return configuration;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Module other = (Module) obj;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name, configuration.getClass().getName());
	}

	public static Module from(InjectionConfiguration injectionConfiguration) {
		return new Module(injectionConfiguration);
	}

	public static Module createModule(String basePackage) {
		Class<?> configurationClass = loadModuleInjectionConfigurationClass(basePackage);
		InjectionConfiguration injectionConfiguration = loadModuleInjectionConfiguration(configurationClass);
		return from(injectionConfiguration);
	}

	private static String loadModuleName(InjectionConfiguration configuration) {
		String packageName = configuration.getClass().getPackage().getName();
		return moduleNameFromPackage(packageName);
	}

	private static String moduleNameFromPackage(String packageName) {
		String finalPackage = StringUtils.substringAfterLast(packageName, ".");
		return StringUtil.capitalize(finalPackage);
	}

	private static InjectionConfiguration loadModuleInjectionConfiguration(Class<?> configurationClass) {
		try {
			Object newInstance = configurationClass.newInstance();
			InjectionConfiguration configuration = Cast.as(newInstance, InjectionConfiguration.class);
			if (configuration == null) {
				throw new ModuleLoadingException(configurationClass.getPackage().getName(), "the configuration class %s does not implement '%s'", configurationClass.getName(), InjectionConfiguration.class.getName());
			}
			return configuration;
		} catch (InstantiationException e) {
			throw new ModuleLoadingException(e, configurationClass.getPackage().getName(), "failed to instantiate configuration class %s: %s", configurationClass.getName(), e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ModuleLoadingException(e, configurationClass.getPackage().getName(), "cannot instantiate configuration class %s: %s", configurationClass.getName(), e.getMessage());
		}
	}

	private static Class<?> loadModuleInjectionConfigurationClass(String modulePackage) {
		String moduleName = moduleNameFromPackage(modulePackage);
		String configurationName = modulePackage + String.format(".%sInjectionConfiguration", moduleName);
		try {
			Class<?> configurationClass = Class.forName(configurationName);
			return configurationClass;
		} catch (ClassNotFoundException e) {
			throw new ModuleLoadingException(e, modulePackage, "the configuration class %s does not exist", configurationName);
		}
	}
}
