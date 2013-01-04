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
	public Map<String, String> load(String filename) {
		try {
			Map<String, String> properties = new LinkedHashMap<String, String>();
			String resourceAsString = Streams.getResourceAsString(filename);
			Scanner scanner = new Scanner(resourceAsString);
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
			return properties;
		} catch (NullPointerException e) {
			throw new ConfigurationException(e, "Failed to load properties from %s: no properties file found", filename);
		} catch (Exception e) {
			throw new ConfigurationException(e, "Failed to load properties from %s: %s", filename, e.getMessage());
		}
	}
}
