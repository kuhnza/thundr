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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.threewks.thundr.util.Streams;

public class PropertiesLoader {
	/**
	 * Loads properties from the given file name as a resource.
	 * 
	 * @param filename
	 * @return
	 * @throws ConfigurationException if the file does not exist or there was a problem parsing the file.
	 */
	public Map<String, String> load(String filename) {
		try {
			String resourceAsString = Streams.getResourceAsString(filename);
			return readProperties(resourceAsString);
		} catch (Exception e) {
			throw new ConfigurationException(e, "Failed to load properties from %s: %s", filename, e.getMessage());
		}
	}

	/**
	 * Loads properties from the given file name as a resource. Returns null if the resource does not exist.
	 * 
	 * @param filename
	 * @return
	 * @throws ConfigurationException if there was a problem parsing the file.
	 */
	public Map<String, String> loadSafe(String filename) {
		try {
			String resourceAsString = Streams.getResourceAsString(filename);
			return readProperties(resourceAsString);
		} catch (Exception e) {
			return null;
		}
	}

	private Map<String, String> readProperties(String resourceAsString) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		Scanner scanner = new Scanner(resourceAsString);
		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line = StringUtils.substringBefore(line, "#");
				line = StringUtils.trimToNull(line);
				String key = StringUtils.substringBefore(line, "=");
				String value = StringUtils.substringAfter(line, "=");
				if (key != null) {
					properties.put(key, value);
				}
			}
		} finally {
			scanner.close();
		}
		return properties;
	}
}
