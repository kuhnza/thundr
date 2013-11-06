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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.InjectionContext;
import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.test.TestInjectionConfiguration;

public class ModulesTest {
	@Rule public ExpectedException thrown = ExpectedException.none();

	private Modules modules = new Modules();
	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();

	@Before
	public void before() {
		injectionContext.inject(modules).as(Modules.class);
	}

	@Test
	public void shouldAddModuleByPackageName() {
		modules.addModule("com.threewks.thundr.module.test");
		assertThat(modules.listModules().size(), is(1));
		assertThat(modules.listModules().get(0) instanceof TestInjectionConfiguration, is(true));
	}

	@Test
	public void shouldAddModuleByClass() {
		modules.addModule(TestInjectionConfiguration.class);
		assertThat(modules.listModules().size(), is(1));
		assertThat(modules.listModules().get(0) instanceof TestInjectionConfiguration, is(true));
	}

	@Test
	public void shouldRetainAddedModules() {
		modules.addModule(TestModule1.class);
		modules.addModule(TestModule3.class);
		assertThat(modules.listModules().size(), is(2));
		assertThat(modules.listModules().get(0) instanceof TestModule1, is(true));
		assertThat(modules.listModules().get(1) instanceof TestModule3, is(true));
	}

	@Test
	public void shouldLoadAddedModules() {
		modules.addModule(TestModule1.class);
		modules.runStartupLifecycle(injectionContext);
		assertThat(injectionContext.get(String.class, "TestModule1"), is("Invoked"));
	}

	@Test
	public void shouldLoadDependentModules() {
		modules.addModule(TestModule1.class);
		modules.addModule(TestModule2.class);
		modules.runStartupLifecycle(injectionContext);
		assertThat(injectionContext.get(String.class, "TestModule1"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule2"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule3"), is("Invoked"));
	}

	@Test
	public void shouldDetectCircularModuleLoading() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("Unable to load modules - there are unloaded modules whose dependencies cannot be satisfied. This probably indicates a cyclical dependency. The following modules have not been loaded: com.threewks.thundr.module.ModulesTest$TestModule4");
		modules.addModule(TestModule4.class);
		modules.runStartupLifecycle(injectionContext);
	}

	@Test
	public void shouldThrowModuleLoadingExceptionWhenGivenModuleIsNotAnInjectionConfiguration() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("Failed to load module 'com.threewks.thundr.module.test.invalid' - the configuration class com.threewks.thundr.module.test.invalid.InvalidInjectionConfiguration does not implement the InjectionConfiguration interface");

		modules.addModule("com.threewks.thundr.module.test.invalid");
	}

	@Test
	public void shouldThrowModuleLoadingExceptionWhenGivenModuleDoesNotExist() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("Failed to load module 'com.threewks.thundr.module.test.non-existant' - the configuration class com.threewks.thundr.module.test.non-existant.Non-existantInjectionConfiguration does not exist");

		modules.addModule("com.threewks.thundr.module.test.non-existant");
	}

	@Test
	public void shouldResolveMoreComplexDependencyGraph() {
		modules.addModule(TestModule5.class);
		modules.resolveDependencies();
		List<InjectionConfiguration> order = modules.determineDependencyOrder();
		assertThat(order.size(), is(4));
		assertThat(order.get(0) instanceof TestModule3, is(true));
		assertThat(order.get(1) instanceof TestModule1, is(true));
		assertThat(order.get(2) instanceof TestModule2, is(true));
		assertThat(order.get(3) instanceof TestModule5, is(true));
	}

	@Test
	public void shouldOnlyAddEachModuleOnce() {
		modules.addModule(TestInjectionConfiguration.class);
		InjectionConfiguration first = modules.listModules().get(0);
		modules.addModule(TestInjectionConfiguration.class);
		InjectionConfiguration second = modules.listModules().get(0);
		assertThat(second, is(sameInstance(first)));
	}

	@Test
	public void shouldRunStartLifecycleOnModules() {
		modules.addModule(TestInjectionConfiguration.class);
		modules.runStartupLifecycle(injectionContext);
		TestInjectionConfiguration testInjectionConfiguration = modules.getModule(TestInjectionConfiguration.class);
		assertThat(testInjectionConfiguration.initialised, is(true));
		assertThat(testInjectionConfiguration.configured, is(true));
		assertThat(testInjectionConfiguration.started, is(true));
		assertThat(testInjectionConfiguration.stopped, is(false));
	}

	@Test
	public void shouldRunStopLifecycleOnModules() {
		modules.addModule(TestInjectionConfiguration.class);
		modules.runStartupLifecycle(injectionContext);
		modules.runStopLifecycle(injectionContext);
		TestInjectionConfiguration testInjectionConfiguration = modules.getModule(TestInjectionConfiguration.class);
		assertThat(testInjectionConfiguration.stopped, is(true));
	}

	@Test
	public void shouldPermitAddingOfModulesWhileModulesLoading() {
		modules.addModule(TestModule6.class);
		modules.runStartupLifecycle(injectionContext);
		assertThat(injectionContext.get(String.class, "TestModule1"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule2"), is("Invoked"));
		assertThat(injectionContext.get(String.class, "TestModule3"), is("Invoked"));
	}

	public static class TestModule1 implements InjectionConfiguration {
		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.inject("Invoked").named("TestModule1").as(String.class);
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}

	public static class TestModule2 implements InjectionConfiguration {
		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
			dependencyRegistry.addDependency(TestModule3.class);
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.inject("Invoked").named("TestModule2").as(String.class);
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}

	public static class TestModule3 implements InjectionConfiguration {

		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			injectionContext.inject("Invoked").named("TestModule3").as(String.class);
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}

	public static class TestModule4 implements InjectionConfiguration {
		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
			dependencyRegistry.addDependency(TestModule4.class);
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}

	public static class TestModule5 implements InjectionConfiguration {
		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
			dependencyRegistry.addDependency(TestModule2.class);
			dependencyRegistry.addDependency(TestModule3.class);
			dependencyRegistry.addDependency(TestModule1.class);
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}

	public static class TestModule6 implements InjectionConfiguration {
		@Override
		public void requires(DependencyRegistry dependencyRegistry) {
		}

		@Override
		public void initialise(UpdatableInjectionContext injectionContext) {
			Modules modules = injectionContext.get(Modules.class);
			modules.addModule(TestModule1.class);
		}

		@Override
		public void configure(UpdatableInjectionContext injectionContext) {
			Modules modules = injectionContext.get(Modules.class);
			modules.addModule(TestModule2.class);
		}

		@Override
		public void start(UpdatableInjectionContext injectionContext) {
			Modules modules = injectionContext.get(Modules.class);
			modules.addModule(TestModule3.class);
		}

		@Override
		public void stop(InjectionContext injectionContext) {
		}
	}
}
