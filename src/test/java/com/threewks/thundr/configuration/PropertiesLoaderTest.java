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

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class PropertiesLoaderTest {
	private PropertiesLoader propertiesLoader = new PropertiesLoader();

	@Test
	public void shouldLoadSimplePropertiesFile() {
		Map<String, String> properties = propertiesLoader.load("simple.properties");
		assertKeysInOrder(properties, "key", "key2");
		assertThat(properties.get("key"), is("value"));
		assertThat(properties.get("key2"), is("value2"));
	}

	@Test
	public void shouldLoadPropertiesWithComments() {
		Map<String, String> properties = propertiesLoader.load("commented.properties");
		assertKeysInOrder(properties, "key", "key2");
		assertThat(properties.get("key"), is("value"));
		assertThat(properties.get("key2"), is("value2"));
	}

	@Test
	public void shouldLoadPropertiesWithDuplicateEntriesTakingLastValue() {
		Map<String, String> properties = propertiesLoader.load("duplicate.properties");
		assertKeysInOrder(properties, "key", "key2");
		assertThat(properties.get("key"), is("last value"));
		assertThat(properties.get("key2"), is("last value again"));
	}

	private void assertKeysInOrder(Map<String, String> properties, String... keys) {
		Set<String> keySet = properties.keySet();
		assertThat(keySet.size(), is(keys.length));

		Iterator<String> keyIterator = keySet.iterator();
		Iterator<String> expectedKeyIterator = list(keys).iterator();
		while (expectedKeyIterator.hasNext()) {
			assertThat(keyIterator.next(), is(expectedKeyIterator.next()));
		}
	}
}
