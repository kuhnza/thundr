package com.atomicleopard.webFramework.configuration;

import java.util.Map;

import com.atomicleopard.webFramework.injection.InjectionConfiguration;
import com.atomicleopard.webFramework.injection.UpdatableInjectionContext;
import com.atomicleopard.webFramework.logger.Logger;

public class ConfigurationInjectionConfiguration implements InjectionConfiguration {
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
		Map<String, String> properties = propertiesLoader.load(filename);
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			injectionContext.inject(String.class).named(key).as(value);
		}
		Logger.debug("Loaded application properties");
	}
}
