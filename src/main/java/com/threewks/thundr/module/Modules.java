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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.transform.CollectionTransformer;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.InjectionContext;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

public class Modules {
	private Map<Class<? extends InjectionConfiguration>, Collection<Class<? extends InjectionConfiguration>>> moduleDependencies = new LinkedHashMap<Class<? extends InjectionConfiguration>, Collection<Class<? extends InjectionConfiguration>>>();
	private Map<Class<? extends InjectionConfiguration>, InjectionConfiguration> instances = new LinkedHashMap<Class<? extends InjectionConfiguration>, InjectionConfiguration>();
	private Map<InjectionConfiguration, ModuleStatus> status = new HashMap<InjectionConfiguration, ModuleStatus>();
	private List<InjectionConfiguration> orderedModules = null;

	public Modules() {
	}

	public void addModule(String modulePackage) {
		Class<? extends InjectionConfiguration> module = Modules.loadModuleInjectionConfigurationClass(modulePackage);
		addModule(module);
	}

	public void addModule(Class<? extends InjectionConfiguration> module) {
		if (!hasModule(module)) {
			InjectionConfiguration instance = loadModule(module);
			moduleDependencies.put(module, null);
			instances.put(module, instance);
			status.put(instance, ModuleStatus.Added);
			Logger.debug("Added module %s", Transformers.toModuleName.from(module));
		}
	}

	public <T extends InjectionConfiguration> T getModule(Class<T> moduleClass) {
		return Cast.as(instances.get(moduleClass), moduleClass);
	}

	public List<? extends InjectionConfiguration> getModules(Collection<Class<? extends InjectionConfiguration>> moduleClasses) {
		List<InjectionConfiguration> result = new ArrayList<InjectionConfiguration>();
		for (Class<? extends InjectionConfiguration> moduleClass : moduleClasses) {
			InjectionConfiguration instance = instances.get(moduleClass);
			if (instance != null) {
				result.add(instance);
			}
		}
		return result;
	}

	public boolean hasModule(Class<? extends InjectionConfiguration> moduleClass) {
		return instances.containsKey(moduleClass);
	}

	public List<InjectionConfiguration> listModules() {
		return new ArrayList<InjectionConfiguration>(instances.values());
	}

	public void runStartupLifecycle(UpdatableInjectionContext injectionContext) {
		Logger.debug("Loading modules...");
		while (!allModulesStarted()) {
			if (status.values().contains(ModuleStatus.Added)) {
				resolveDependencies();
				orderedModules = determineDependencyOrder();
			} else {
				boolean allInitialised = initialiseNext(injectionContext);
				boolean allConfigured = false;
				if (allInitialised) {
					allConfigured = configureNext(injectionContext);
				}
				if (allConfigured) {
					startNext(injectionContext);
				}
			}
		}
		Logger.info("Modules loaded");
	}

	/**
	 * @param injectionContext
	 * @return true if all modules are initialised
	 */
	private boolean initialiseNext(UpdatableInjectionContext injectionContext) {
		InjectionConfiguration initialise = getFirstModuleWithStatus(ModuleStatus.DependenciesResolved);
		if (initialise != null) {
			initialise.initialise(injectionContext);
			status.put(initialise, ModuleStatus.Initialised);
		}
		return initialise == null;
	}

	/**
	 * @param injectionContext
	 * @return true if all modules are already initialised
	 */
	private boolean configureNext(UpdatableInjectionContext injectionContext) {
		InjectionConfiguration configure = getFirstModuleWithStatus(ModuleStatus.Initialised);
		if (configure != null) {
			configure.configure(injectionContext);
			status.put(configure, ModuleStatus.Configured);
		}
		return configure == null;
	}

	private boolean startNext(UpdatableInjectionContext injectionContext) {
		InjectionConfiguration start = getFirstModuleWithStatus(ModuleStatus.Configured);
		if (start != null) {
			start.start(injectionContext);
			status.put(start, ModuleStatus.Started);
		}
		return start == null;
	}

	private InjectionConfiguration getFirstModuleWithStatus(ModuleStatus status) {
		InjectionConfiguration result = null;
		for (InjectionConfiguration injectionConfiguration : orderedModules) {
			if (status.equals(this.status.get(injectionConfiguration))) {
				return injectionConfiguration;
			}
		}
		return result;
	}

	private boolean allModulesStarted() {
		Collection<ModuleStatus> values = status.values();
		return !values.contains(ModuleStatus.Added) && !values.contains(ModuleStatus.DependenciesResolved) && !values.contains(ModuleStatus.Initialised) && !values.contains(ModuleStatus.Configured);
	}

	public void runStopLifecycle(InjectionContext injectionContext) {
		List<InjectionConfiguration> reverseOrder = new LinkedList<InjectionConfiguration>(orderedModules);
		Collections.reverse(reverseOrder);
		for (InjectionConfiguration injectionConfiguration : reverseOrder) {
			injectionConfiguration.stop(injectionContext);
			status.put(injectionConfiguration, ModuleStatus.Stopped);
		}
	}

	/**
	 * Determines the dependency order of all modules.
	 * 
	 * @return
	 */
	protected List<InjectionConfiguration> determineDependencyOrder() {
		List<InjectionConfiguration> orderedModules = new ArrayList<InjectionConfiguration>();

		while (!orderedModules.containsAll(instances.values())) {
			boolean anyAdded = false;
			for (Map.Entry<Class<? extends InjectionConfiguration>, InjectionConfiguration> entry : instances.entrySet()) {
				InjectionConfiguration instance = entry.getValue();

				if (!orderedModules.contains(instance)) {
					Class<? extends InjectionConfiguration> configurationClass = entry.getKey();

					Collection<Class<? extends InjectionConfiguration>> dependencies = moduleDependencies.get(configurationClass);
					List<? extends InjectionConfiguration> injectionConfigurations = getModules(dependencies);
					if (orderedModules.containsAll(injectionConfigurations)) {
						orderedModules.add(instance);
						anyAdded = true;
					}
				}
			}
			if (!anyAdded) {
				List<InjectionConfiguration> unloaded = Expressive.list(instances.values()).removeItems(orderedModules);
				EList<String> moduleNames = Transformers.toModuleNamesFromInstance.from(unloaded);
				throw new ModuleLoadingException(
						"",
						"Unable to load modules - there are unloaded modules whose dependencies cannot be satisfied. This probably indicates a cyclical dependency. The following modules have not been loaded: %s",
						StringUtils.join(moduleNames, " "));
			}
		}
		return orderedModules;
	}

	/**
	 * Causes the dependent modules for any modules already added to be added as well.
	 */
	public void resolveDependencies() {
		while (hasMoreDependenciesToResolve()) {
			for (InjectionConfiguration injectionConfiguration : getModulesWithUnresolvedDependencies()) {

				Class<? extends InjectionConfiguration> moduleClass = injectionConfiguration.getClass();

				DependencyRegistry dependencyRegistry = new DependencyRegistry();
				injectionConfiguration.requires(dependencyRegistry);
				Collection<Class<? extends InjectionConfiguration>> dependencies = dependencyRegistry.getDependencies();
				String moduleName = Transformers.toModuleName.from(moduleClass);
				for (Class<? extends InjectionConfiguration> dependencyClass : dependencies) {
					addModule(dependencyClass);
					Logger.debug("Module %s depends on %s", moduleName, Transformers.toModuleName.from(dependencyClass));
				}

				moduleDependencies.put(moduleClass, dependencies);
				status.put(injectionConfiguration, ModuleStatus.DependenciesResolved);
			}
		}
	}

	private Collection<InjectionConfiguration> getModulesWithUnresolvedDependencies() {
		Collection<InjectionConfiguration> result = new ArrayList<InjectionConfiguration>();
		for (Map.Entry<InjectionConfiguration, ModuleStatus> loaded : status.entrySet()) {
			if (ModuleStatus.Added.equals(loaded.getValue())) {
				result.add(loaded.getKey());
			}
		}
		return result;
	}

	private boolean hasMoreDependenciesToResolve() {
		return status.values().contains(ModuleStatus.Added);
	}

	protected static InjectionConfiguration loadModule(Class<? extends InjectionConfiguration> moduleClass) {
		try {
			Object newInstance = moduleClass.newInstance();
			InjectionConfiguration configuration = Cast.as(newInstance, InjectionConfiguration.class);
			if (configuration == null) {
				throw new ModuleLoadingException(moduleClass.getPackage().getName(), "the configuration class %s does not implement '%s'", moduleClass.getName(),
						InjectionConfiguration.class.getName());
			}
			return configuration;
		} catch (InstantiationException e) {
			throw new ModuleLoadingException(e, moduleClass.getPackage().getName(), "failed to instantiate configuration class %s: %s", moduleClass.getName(), e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ModuleLoadingException(e, moduleClass.getPackage().getName(), "cannot instantiate configuration class %s: %s", moduleClass.getName(), e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected static Class<? extends InjectionConfiguration> loadModuleInjectionConfigurationClass(String modulePackage) {
		String className = StringUtils.capitalize(StringUtils.substringAfterLast(modulePackage, "."));
		String fullClassName = modulePackage + String.format(".%sInjectionConfiguration", className);
		try {
			Class<?> configurationClass = Class.forName(fullClassName);
			if (!InjectionConfiguration.class.isAssignableFrom(configurationClass)) {
				throw new ModuleLoadingException(modulePackage, "the configuration class %s does not implement the %s interface", fullClassName, InjectionConfiguration.class.getSimpleName());
			}
			return (Class<? extends InjectionConfiguration>) configurationClass;
		} catch (ClassNotFoundException e) {
			throw new ModuleLoadingException(e, modulePackage, "the configuration class %s does not exist", fullClassName);
		}
	}

	public static class Transformers {
		public static final ETransformer<Class<?>, String> toModuleName = new ETransformer<Class<?>, String>() {
			@Override
			public String from(Class<?> from) {
				return from.getName();
			}
		};
		public static final CollectionTransformer<Class<?>, String> toModuleNames = Expressive.Transformers.transformAllUsing(toModuleName);
		public static final ETransformer<InjectionConfiguration, String> toModuleNameFromInstance = new ETransformer<InjectionConfiguration, String>() {
			@Override
			public String from(InjectionConfiguration from) {
				return from.getClass().getName();
			}
		};
		public static final CollectionTransformer<InjectionConfiguration, String> toModuleNamesFromInstance = Expressive.Transformers.transformAllUsing(toModuleNameFromInstance);
	}

	private enum ModuleStatus {
		Added,
		DependenciesResolved,
		Initialised,
		Configured,
		Started,
		Stopped;
	}
}
