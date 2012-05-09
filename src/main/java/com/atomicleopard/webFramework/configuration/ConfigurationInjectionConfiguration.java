package com.atomicleopard.webFramework.configuration;

import static com.google.appengine.api.utils.SystemProperty.environment;

import java.util.Map;
import java.util.regex.Pattern;

import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;

public class ConfigurationInjectionConfiguration implements InjectionConfiguration {
	public static final String EnvironmentProperty = "environment";
	public static final String EnvironmentPropertyOverride = "environmentOverride";

	private static final Map<Value, Environment> standardEnvironments = Expressive.mapKeys(Value.Production, Value.Development, null).to(Environment.Production, Environment.Development, Environment.Development);

	private PropertiesLoader propertiesLoader = new PropertiesLoader();
	private String filename;

	public ConfigurationInjectionConfiguration() {
		this("application.properties");
	}

	public ConfigurationInjectionConfiguration(String filename) {
		this.filename = filename;
	}

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		Logger.info("Loading application properties from %s", filename);
		Map<String, String> properties = loadProperties();
		Environment environment = detectEnvironment(properties);
		Logger.info("Running as %s environment", environment.getEnvironment());
		injectionContext.inject(Environment.class).as(environment);
		injectPropertiesBasedOnEnvironment(injectionContext, properties, environment);
		Logger.debug("Loaded application properties");
	}

	protected String filter(String key, Environment environment) {
		if (key == null) {
			return null;
		}
		String filteredKey = key.replaceAll(Pattern.quote("%" + environment.getEnvironment()), "");
		if (filteredKey.contains("%")) {
			return null;
		}
		return filteredKey;
	}

	protected Environment detectEnvironment(Map<String, String> properties) {
		String override = properties.get(EnvironmentPropertyOverride);
		if (override != null) {
			return new Environment(override);
		}
		return standardEnvironments.get(environment.value());
	}

	private Map<String, String> loadProperties() {
		return propertiesLoader.load(filename);
	}

	private void injectPropertiesBasedOnEnvironment(UpdatableInjectionContext injectionContext, Map<String, String> properties, Environment environment) {
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			String filteredKey = filter(key, environment);
			if (filteredKey != null) {
				injectionContext.inject(String.class).named(filteredKey).as(value);
			}
		}
	}
}
