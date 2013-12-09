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
package com.threewks.thundr.action.method.bind.http;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.collection.factory.SimpleCollectionFactory;
import com.threewks.thundr.introspection.ParameterDescription;

public class CollectionParameterBinderTest {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CollectionParameterBinder<List<Object>> listParameterBinder = new CollectionParameterBinder<List<Object>>(new SimpleCollectionFactory(List.class, ArrayList.class));

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CollectionParameterBinder<Collection<Object>> collectionParameterBinder = new CollectionParameterBinder<Collection<Object>>(new SimpleCollectionFactory(Collection.class, ArrayList.class));
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CollectionParameterBinder<Set<Object>> setParameterBinder = new CollectionParameterBinder<Set<Object>>(new SimpleCollectionFactory(Set.class, LinkedHashSet.class));

	@Test
	public void shouldBindEmptyListEntriesToNull() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Collections.emptyMap();
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName", new String[0]);
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName", new String[] { null });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName", new String[] { "" });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));
	}

	@Test
	public void shouldBindSingleIndexedEmptyOrNullEntryToNull() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName[0]", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Collections.emptyMap();
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[0]", new String[0]);
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[0]", new String[] { null });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[0]", new String[] { "" });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));
	}

	@Test
	public void shouldBindSingleUnindexedEmptyOrNullEntryToNull() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName[]", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Collections.emptyMap();
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[]", new String[0]);
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[]", new String[] { null });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));

		data = Collections.singletonMap("paramName[]", new String[] { "" });
		pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(nullValue()));
	}

	@Test
	public void shouldBindUnindexedListEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(List.class));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(Arrays.<Object> asList("first", "second", "third")));
	}

	@Test
	public void shouldBindUnindexedJsonStyleListEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName[]", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(List.class));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(Arrays.<Object> asList("first", "second", "third")));
	}

	@Test
	public void shouldBindIndexedStyleListEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[2]", new String[] { "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(List.class));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(Arrays.<Object> asList("first", "second", "third")));
	}

	@Test
	public void shouldBindSparselyIndexedStyleListEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.ListOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[4]", new String[] { "fifth" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(List.class));
		assertThat(listParameterBinder.bind(binders, parameterDescription, pathMap), is(Arrays.<Object> asList("first", "second", null, null, "fifth")));
	}

	@Test
	public void shouldBindUnindexedCollectionEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.CollectionOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Collection.class));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.<Object> hasItems("first", "second", "third"));
	}

	@Test
	public void shouldBindUnindexedJsonStyleCollectionEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.CollectionOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName[]", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Collection.class));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.<Object> hasItems("first", "second", "third"));
	}

	@Test
	public void shouldBindIndexedStyleCollectionEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.CollectionOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[2]", new String[] { "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Collection.class));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.<Object> hasItems("first", "second", "third"));
	}

	@Test
	public void shouldBindSparselyIndexedStyleCollectionEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.CollectionOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[4]", new String[] { "fifth" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Collection.class));
		assertThat(collectionParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.<Object> hasItems("first", "second", null, null, "fifth"));
	}

	@Test
	public void shouldBindUnindexedSetEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.SetOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Set.class));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(Expressive.<Object> set("first", "second", "third")));
	}

	@Test
	public void shouldBindUnindexedJsonStyleSetEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.SetOfStrings);
		Map<String, String[]> data = Collections.singletonMap("paramName[]", new String[] { "first", "second", "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Set.class));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(Expressive.<Object> set("first", "second", "third")));
	}

	@Test
	public void shouldBindIndexedStyleSetEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.SetOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[2]", new String[] { "third" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Set.class));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(Expressive.<Object> set("first", "second", "third")));
	}

	@Test
	public void shouldBindSparselyIndexedStyleSetEntries() {
		ParameterBinderSet binders = new ParameterBinderSet();
		ParameterDescription parameterDescription = new ParameterDescription("paramName", TypeProvider.SetOfStrings);
		Map<String, String[]> data = Expressive.map();
		data.put("paramName[0]", new String[] { "first" });
		data.put("paramName[1]", new String[] { "second" });
		data.put("paramName[4]", new String[] { "fifth" });
		HttpPostDataMap pathMap = new HttpPostDataMap(data);
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(notNullValue()));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), Matchers.isA(Set.class));
		assertThat(setParameterBinder.bind(binders, parameterDescription, pathMap), is(Expressive.<Object> set("first", "second", null, "fifth")));
	}

	private static class TypeProvider {
		public static Type ListOfStrings;
		public static Type CollectionOfStrings;
		public static Type SetOfStrings;
		static {
			try {
				ListOfStrings = TypeProvider.class.getMethod("listOfStrings").getGenericReturnType();
				CollectionOfStrings = TypeProvider.class.getMethod("collectionOfStrings").getGenericReturnType();
				SetOfStrings = TypeProvider.class.getMethod("setOfStrings").getGenericReturnType();
			} catch (Exception e) {
				throw new RuntimeException("Failed while attempting to generated generic types for testing: " + e.getMessage(), e);
			}
		}

		@SuppressWarnings("unused")
		public List<String> listOfStrings() {
			return null;
		}

		@SuppressWarnings("unused")
		public Collection<String> collectionOfStrings() {
			return null;
		}

		@SuppressWarnings("unused")
		public Set<String> setOfStrings() {
			return null;
		}
	}
}
