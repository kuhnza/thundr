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
