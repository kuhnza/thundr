package com.threewks.thundr.configuration;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.utils.SystemProperty;
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
	
	@After
	public void after() {
	    SystemProperty.environment.set(SystemProperty.Environment.Value.Development.name());
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
	public void shouldInjectEnvironmentSpecificPropertiesIntoInjectionContext() {
	    SystemProperty.environment.set(SystemProperty.Environment.Value.Development.name());
		Map<String, String> properties = map("property1%my-application-id", "property 1 value", "property2%dev", "property 2 value");
		setUpProperties(properties);
		configuration.configure(injectionContext);
		assertThat(injectionContext.get(String.class, "property2"), is("property 2 value"));
		assertThat(injectionContext.contains(String.class, "property1"), is(false));
		assertThat(injectionContext.contains(String.class, "property1%my-application-id"), is(false));
	}
	
	@Test
	public void shouldPreferEnvironmentSpecificPropertiesOverBaseProperties() {
	    SystemProperty.environment.set(SystemProperty.Environment.Value.Development.name());
	    Map<String, String> properties = map("property1%dev", "property 1 development value", "property1", "property 1 base value");
        setUpProperties(properties);
        configuration.configure(injectionContext);
        assertThat(injectionContext.get(String.class, "property1"), is("property 1 development value"));
	}
	
	@Test
	public void shouldTrimKeys() {
	    assertThat(configuration.filter("key%dev", "dev"), is("key"));
	    assertThat(configuration.filter("key%Dev", "dev"), is(nullValue()));
	    assertThat(configuration.filter("key%dev%dev", "dev"), is(nullValue()));
	    assertThat(configuration.filter("key", "dev"), is("key"));
	    assertThat(configuration.filter(null, "dev"), is(nullValue()));
	}
	
	@Test
	public void shouldGetApplicationIdInDevelopmentMode() {
	    SystemProperty.environment.set(SystemProperty.Environment.Value.Development.name());
	    SystemProperty.applicationId.set("my-application-id");
	    assertThat(configuration.getApplicationId(), is("dev"));
	}

	@Test
	public void shouldGetApplicationIdInProductionMode() {
	    SystemProperty.environment.set(SystemProperty.Environment.Value.Production.name());
	    SystemProperty.applicationId.set("my-application-id");
	    assertThat(configuration.getApplicationId(), is("my-application-id"));
	}
	
	private void setUpProperties(Map<String, String> value) {
		when(propertiesLoader.load(anyString())).thenReturn(value);
	}
}
