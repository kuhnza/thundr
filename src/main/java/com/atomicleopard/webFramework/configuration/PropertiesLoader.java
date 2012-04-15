package com.atomicleopard.webFramework.configuration;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.atomicleopard.webFramework.util.Streams;

public class PropertiesLoader {
	public Map<String, String> load(String filename) {
		Properties loadProperties = loadProperties(filename);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, String> properties = (Map) loadProperties;
		return new LinkedHashMap<String, String>(properties);
	}

	public Properties loadProperties(String filename) {
		try {
			Properties properties = new Properties();
			InputStream propertiesStream = Streams.getResourceAsStream(filename);
			properties.load(propertiesStream);
			return properties;
		} catch (NullPointerException e) {
			throw new ConfigurationException(e, "Failed to load properties from %s: no properties file found", filename);
		} catch (Exception e) {
			throw new ConfigurationException(e, "Failed to load properties from %s: %s", filename, e.getMessage());
		}
	}
}
