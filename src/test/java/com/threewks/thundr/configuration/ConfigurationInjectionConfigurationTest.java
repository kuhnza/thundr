package com.threewks.thundr.configuration;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.injection.InjectionContextImpl;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.test.TestSupport;

public class ConfigurationInjectionConfigurationTest {
	private ConfigurationInjectionConfiguration configuration = new ConfigurationInjectionConfiguration();
	private UpdatableInjectionContext injectionContext = new InjectionContextImpl();
	private PropertiesLoader propertiesLoader = mock(PropertiesLoader.class);

	@Before
	public void before() {
		TestSupport.setField(configuration, "propertiesLoader", propertiesLoader);
	}

	@Test
	public void shouldInjectPropertiesIntoInjectionContext() {
		Map<String, String> properties = map("property1", "property 1 value", "property2", "property 2 value");
		setUpProperties(properties);
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(String.class, "property1"), is("property 1 value"));
		assertThat(injectionContext.get(String.class, "property2"), is("property 2 value"));
	}

	@Test
	public void shouldInjectEnvironmentSpecifiecPropertiesIntoInjectionContext() {
		Map<String, String> properties = map("property1%Production", "property 1 value", "property2%Development", "property 2 value");
		setUpProperties(properties);
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(String.class, "property2"), is("property 2 value"));
		assertThat(injectionContext.contains(String.class, "property1"), is(false));
		assertThat(injectionContext.contains(String.class, "property1%Production"), is(false));
	}
	
	@Test
	public void shouldAllowEnvironmentOverride() {
		Map<String, String> properties = map(ConfigurationInjectionConfiguration.EnvironmentPropertyOverride, "Production", "property1%Production", "property 1 value", "property2%Development", "property 2 value");
		setUpProperties(properties);
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(String.class, "property1"), is("property 1 value"));
		assertThat(injectionContext.contains(String.class, "property2"), is(false));
	}
	
	@Test
	public void shouldAllowArbitrayNamedEnvironment() {
		Map<String, String> properties = map(ConfigurationInjectionConfiguration.EnvironmentPropertyOverride, "Barry", "property1%Barry", "property 1 value", "property2%Development", "property 2 value");
		setUpProperties(properties);
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(String.class, "property1"), is("property 1 value"));
		assertThat(injectionContext.contains(String.class, "property2"), is(false));
	}
	
	@Test
	public void shouldInjectEnvironmentIntoInjectionContext() {
		setUpProperties(Collections.<String, String>emptyMap());
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(Environment.class).getEnvironment(), is("Development"));
	}
	
	@Test
	public void shouldNotFailOnNullPropertyEntries() {
		assertThat(configuration.filter(null, new Environment("env")), is(nullValue()));
		assertThat(configuration.filter("", new Environment("env")), is(""));
		assertThat(configuration.filter(" ", new Environment("env")), is(" "));
	}
	
	@Test
	public void shouldReturnNullFromFilterKeysIfNotForMatchingEnvironment() {
		assertThat(configuration.filter("key", new Environment("env")), is("key"));
		assertThat(configuration.filter("key%env", new Environment("env")), is("key"));
		assertThat(configuration.filter("key%different", new Environment("env")), is(nullValue()));
		assertThat(configuration.filter("key%", new Environment("env")), is(nullValue()));
		assertThat(configuration.filter("key%env%somethingelse", new Environment("env")), is(nullValue()));
		assertThat(configuration.filter("%differentkey", new Environment("env")), is(nullValue()));
		assertThat(configuration.filter("%key", new Environment("env")), is(nullValue()));
	}
	

	private void setUpProperties(Map<String, String> value) {
		when(propertiesLoader.load(anyString())).thenReturn(value);
	}
}
