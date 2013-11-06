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

import com.threewks.thundr.module.DependencyRegistry;

/**
 * Defines the configuration required by and provided by a thundr module - this also includes applications.
 * 
 * When an application begins, the following lifecycle is followed:
 * <ol>
 *  <li>{@link #requires() is invoked in dependency order on all added modules - this allows modules to express a dependency on an load other modules programatically. Dependency order is adhered to by the other lifecycle methods</li>
 * 	<li>{@link #initialise(UpdatableInjectionContext)} is invoked in dependency order on all modules - modules can provide configuration and implementations that have no dependencies here by injecting them into the InjectionContext</li>
 * 	<li>{@link #configure(UpdatableInjectionContext) is invoked in dependency order on all modules - modules complete configuration of provided classes here  - any dependent interfaces or configuration should already have been provided</li>
 * 	<li>{@link #start(UpdatableInjectionContext) is invoked in dependency order on all modules - modules that need to execute logic on startup, start services or thread pools etc can do this here - all configuration should already have been provided</li>
 * </ol>
 * 
 * Likewise, when an application is stopped, the following lifecylce is followed:
 * <ol>
 * <li>{@link #stop(UpdatableInjectionContext) is invoked in reverse dependency order on all modules - modules that need to execute logic on stopping, stop services or clean up can do this here</li>
 * </ol>
 * 
 * @author nick
 * 
 */
public interface InjectionConfiguration {
	/**
	 * The first startup lifecycle step. This {@link InjectionConfiguration} can specify any dependencies it has on the given {@link DependencyRegistry}.
	 * 
	 * This may result in additional modules being added to the application and changes in dependency order for subsequent lifecycle methods.
	 * 
	 * @param dependencyRegistry
	 */
	public void requires(DependencyRegistry dependencyRegistry);

	/**
	 * The second startup lifecycle step. This {@link InjectionConfiguration} should provide any instances and classes into the injection context which
	 * do not have any dependencies on other modules. Examples include loading properties, adding implementations which are not dynamic etc.
	 * 
	 * This method is invoked on all modules in dependency order.
	 * 
	 * @param injectionContext
	 */
	public void initialise(UpdatableInjectionContext injectionContext);

	/**
	 * The third startup lifecycle step. This {@link InjectionConfiguration} should complete the provisioning of instances and interfaces into the injection context here.
	 * At this point all dependent instances and classes for this module should have already been provided in the injection context.
	 * 
	 * This method is invoked on all modules in dependency order.
	 * 
	 * @param injectionContext
	 */
	public void configure(UpdatableInjectionContext injectionContext);

	/**
	 * The fourth and final startup lifecycle step. This {@link InjectionConfiguration} can use this lifecycle phase to start any services etc.
	 * 
	 * This method is invoked on all modules in dependency order.
	 * 
	 * @param injectionContext
	 */
	public void start(UpdatableInjectionContext injectionContext);

	/**
	 * The first and only step in the shutdown lifecycle. This {@link InjectionConfiguration} can use this lifecycle phase to stop and cleanup any services.
	 * 
	 * This method is invoked on all modules in reverse dependency order.
	 * 
	 * @param injectionContext
	 */
	public void stop(InjectionContext injectionContext);
}
