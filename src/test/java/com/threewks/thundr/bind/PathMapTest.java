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
package com.threewks.thundr.bind;
import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.threewks.thundr.action.method.bind.http.HttpPostDataMap;

public class PathMapTest {
	@Test
	public void shouldNotSplitWhenSimple() {
		Map<String, String[]> map = map("key", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("key")), is(array("value")));
	}

	@Test
	public void shouldSplitForNestedPath() {
		Map<String, String[]> map = map("one.two.three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("one", "two", "three")), is(array("value")));
	}

	@Test
	public void shouldSplitForNestedListPath() {
		Map<String, String[]> map = map("one[two].three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		assertThat(pathMap.get(list("one", "[two]", "three")), is(array("value")));
	}

	@Test
	public void shouldCreateANewPathMapForNestedPath() {
		Map<String, String[]> map = map("one[two].three", new String[] { "value" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		HttpPostDataMap newPathMap = pathMap.pathMapFor("one");
		assertThat(newPathMap.get(list("[two]", "three")), is(array("value")));
	}

	@Test
	public void shouldCreateANewPathMapForNestedPathRemovingUnrelatedPaths() {
		Map<String, String[]> map = mapKeys("one[two].three", "one[one].two", "other.thing").to(new String[] { "value" }, new String[] { "value2" }, new String[] { "value3" });
		HttpPostDataMap pathMap = new HttpPostDataMap(map);
		HttpPostDataMap newPathMap = pathMap.pathMapFor("one");
		assertThat(newPathMap.size(), is(2));
		assertThat(newPathMap.get(list("[two]", "three")), is(array("value")));
		assertThat(newPathMap.get(list("[one]", "two")), is(array("value2")));
	}
}
