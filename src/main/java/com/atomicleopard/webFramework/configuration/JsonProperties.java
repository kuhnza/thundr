package com.atomicleopard.webFramework.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.webFramework.json.JsonToMapDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class JsonProperties {

	private Map<String, Object> properties;

	@SuppressWarnings("unchecked")
	public JsonProperties(String source) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Object.class, new JsonToMapDeserializer());
		Gson gson = gsonBuilder.create();
		try {
			properties = gson.fromJson(source, Map.class);
		} catch (JsonSyntaxException e) {
			throw new ConfigurationException(e, "Failed to read json properties: %s", e.getMessage());
		}
	}

	public List<String> getKeys() {
		return new ArrayList<String>(properties.keySet());
	}

	public String getString(String key) {
		return getAs(key, String.class);
	}

	public String getString(String key, String defaultValue) {
		return getAs(key, String.class, defaultValue);
	}

	public boolean is(String key, Class<?> class1) {
		return Cast.is(properties.get(key), class1);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key) {
		return getAs(key, List.class);
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap(String key) {
		return getAs(key, Map.class);
	}

	private <T> T getAs(String key, Class<T> class1, T defaultValue) {
		Object storedValue = properties.get(key);
		T value = Cast.as(storedValue, class1);
		return value == null ? defaultValue : value;
	}

	private <T> T getAs(String key, Class<T> class1) {
		Object storedValue = properties.get(key);
		T value = Cast.as(storedValue, class1);
		if (value == null) {
			throw new ConfigurationException("Could not get property '%s' - required a '%s' but had '%s'", key, typeof(class1), typeof(storedValue));
		}
		return value;
	}

	private <T> String typeof(Class<T> class1) {
		return class1.getSimpleName();
	}

	private String typeof(Object object) {
		return object == null ? "null" : object.getClass().getSimpleName();
	}
}
