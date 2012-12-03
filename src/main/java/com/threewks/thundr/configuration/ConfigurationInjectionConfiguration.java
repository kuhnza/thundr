package com.threewks.thundr.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.logger.Logger;

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
		Map<String, String> properties = loadProperties();

		injectPropertiesBased(injectionContext, properties);

		Logger.debug("Loaded application properties");
	}

	private Map<String, String> loadProperties() {
		return propertiesLoader.load(filename);
	}

	private void injectPropertiesBased(UpdatableInjectionContext injectionContext, Map<String, String> properties) {
		List<String> keys = new ArrayList<String>(properties.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = properties.get(key);
			injectionContext.inject(value).named(key).as(String.class);
		}
	}

}
