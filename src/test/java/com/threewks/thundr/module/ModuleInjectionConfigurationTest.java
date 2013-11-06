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

import static com.atomicleopard.expressive.Expressive.mapKeys;
import static com.threewks.thundr.test.TestSupport.setField;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.configuration.PropertiesLoader;
import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.module.test.TestInjectionConfiguration;
import com.threewks.thundr.module.test.m1.M1InjectionConfiguration;
import com.threewks.thundr.module.test.m2.M2InjectionConfiguration;

public class ModuleInjectionConfigurationTest {

	@Rule public ExpectedException thrown = ExpectedException.none();

	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();
	private ModuleInjectionConfiguration config = new ModuleInjectionConfiguration();
	private Modules modules = new Modules();
	private PropertiesLoader propertyLoader;

	@Before
	public void before() {
		injectionContext.inject(modules).as(Modules.class);
		propertyLoader = mock(PropertiesLoader.class);
		when(propertyLoader.loadSafe(anyString())).thenReturn(mapKeys("com.threewks.thundr.module.test").to(""));
		setField(config, "propertiesLoader", propertyLoader);
	}

	@Test
	public void shouldAddModuleForEntryInProperties() {
		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(1));
		assertThat(modules.listModules().get(0) instanceof TestInjectionConfiguration, is(true));

		assertThat(modules.hasModule(TestInjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldLoadModulesInReverseOrderFromProperties() {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		properties.put("com.threewks.thundr.module.test.m2", "");
		properties.put("com.threewks.thundr.module.test.m1", "");
		when(propertyLoader.loadSafe(anyString())).thenReturn(properties);
		setField(config, "propertiesLoader", propertyLoader);

		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(2));

		assertThat(modules.hasModule(M1InjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(M2InjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldAddAModuleForEachEntryInProperties() {
		Map<String, String> properties = mapKeys("com.threewks.thundr.module", "com.threewks.thundr.module.test").to("", "");
		when(propertyLoader.loadSafe(anyString())).thenReturn(properties);

		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(2));

		assertThat(modules.hasModule(TestInjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(ModuleInjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldLoadModuleFromInjectionContext() {
		when(propertyLoader.loadSafe(anyString())).thenReturn(null);
		injectionContext.inject("com.threewks.thundr.module.test.m1").named(ModuleInjectionConfiguration.PropertyNameModules).as(String.class);

		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(1));
		assertThat(modules.hasModule(M1InjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldLoadModulesFromInjectionContextAsConcategnatedStrings() {
		when(propertyLoader.loadSafe(anyString())).thenReturn(null);
		String concatenatedModuleNames = "com.threewks.thundr.module.test.m1;com.threewks.thundr.module.test.m2,com.threewks.thundr.module.test:com.threewks.thundr.module";
		injectionContext.inject(concatenatedModuleNames).named(ModuleInjectionConfiguration.PropertyNameModules).as(String.class);

		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(4));
		assertThat(modules.hasModule(M1InjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(M2InjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(TestInjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(ModuleInjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldLoadModulesFromInjectionContextAsListOfStrings() {
		when(propertyLoader.loadSafe(anyString())).thenReturn(null);
		List<String> moduleNames = Arrays.asList("com.threewks.thundr.module.test.m1", "com.threewks.thundr.module.test.m2");
		injectionContext.inject(moduleNames).named(ModuleInjectionConfiguration.PropertyNameModules).as(List.class);

		config.initialise(injectionContext);
		assertThat(modules.listModules().size(), is(2));
		assertThat(modules.hasModule(M1InjectionConfiguration.class), is(true));
		assertThat(modules.hasModule(M2InjectionConfiguration.class), is(true));
	}

	@Test
	public void shouldThrowAnExceptionIfNoModulesConfigured() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("No modules have been specified - you must minimally specify your application module in either your application configuration property 'thundrModules' or the file 'modules.properties'");
		when(propertyLoader.loadSafe(anyString())).thenReturn(Collections.<String, String> emptyMap());
		config.initialise(injectionContext);
	}

}
