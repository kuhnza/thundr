package com.atomicleopard.webFramework.module;

import static com.atomicleopard.expressive.Expressive.*;
import static com.atomicleopard.webFramework.test.TestSupport.setField;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;
import com.atomicleopard.webFramework.configuration.PropertiesLoader;
import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.InjectionContextImpl;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.module.test.TestInjectionConfiguration;

public class ModuleInjectionConfigurationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();
	private ModuleInjectionConfiguration config = new ModuleInjectionConfiguration();
	private Modules modules = new Modules();
	private PropertiesLoader propertyLoader;

	@Before
	public void before() {
		injectionContext.inject(Modules.class).as(modules);
		propertyLoader = mock(PropertiesLoader.class);
		when(propertyLoader.load(anyString())).thenReturn(mapKeys("com.atomicleopard.webFramework.module.test").to(""));
		setField(config, "propertiesLoader", propertyLoader);
	}

	@Test
	public void shouldAddModuleForEntryInProperties() {
		config.configure(injectionContext);
		assertThat(modules.listModules().size(), is(1));
		assertThat(Cast.is(modules.listModules().get(0).getConfiguration(), TestInjectionConfiguration.class), is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddAModuleForEachEntryInProperties() {
		Map<String, String> properties = mapKeys("com.atomicleopard.webFramework.module", "com.atomicleopard.webFramework.module.test").to("", "");
		when(propertyLoader.load(anyString())).thenReturn(properties);

		config.configure(injectionContext);
		assertThat(modules.listModules().size(), is(2));
		EList<Class<? extends InjectionConfiguration>> classes = list();
		classes.add(modules.listModules().get(0).getConfiguration().getClass());
		classes.add(modules.listModules().get(1).getConfiguration().getClass());
		assertThat(classes, hasItems(ModuleInjectionConfiguration.class, TestInjectionConfiguration.class));
	}

	@Test
	public void shouldOnlyLoadModulesOnce() {
		config.configure(injectionContext);
		assertThat(modules.listModules().size(), is(1));
		InjectionConfiguration configuration = modules.listModules().get(0).getConfiguration();
		TestInjectionConfiguration testConfig = (TestInjectionConfiguration) configuration;
		assertThat(testConfig.loaded, is(false));
		modules.loadModules(injectionContext);
		assertThat(testConfig.loaded, is(true));
		testConfig.loaded = false;
		modules.loadModules(injectionContext);
		assertThat(testConfig.loaded, is(false));
	}

	@Test
	public void shouldThrowAnExceptionIfNoModulesConfigured() {
		thrown.expect(ModuleLoadingException.class);
		thrown.expectMessage("you must have an entry for your application configuration in modules.properties");
		when(propertyLoader.load(anyString())).thenReturn(Collections.<String, String> emptyMap());
		config.configure(injectionContext);
	}

}
