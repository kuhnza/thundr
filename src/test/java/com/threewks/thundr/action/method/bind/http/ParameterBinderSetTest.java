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

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.threewks.thundr.introspection.ParameterDescription;

public class ParameterBinderSetTest {

	private TestParameterBinder registeredBinder;

	@After
	public void after() {
		if (registeredBinder != null) {
			ParameterBinderSet.unregisterGlobalBinder(registeredBinder);
		}
	}

	@Test
	public void shouldReturnNullWhenCannotBeBound() {
		ParameterBinderSet parameterBinderSet = new ParameterBinderSet();

		Map<String, String[]> map = map("string", new String[] { "value" });
		assertThat(parameterBinderSet.createFor(new ParameterDescription("string", Void.class), new HttpPostDataMap(map)), is(nullValue()));
	}

	@Test
	public void shouldAllowAdditionOfBindersForBinderInstance() {
		ParameterBinderSet parameterBinderSet = new ParameterBinderSet();

		Map<String, String[]> map = map("string", new String[] { "value" });
		assertThat(parameterBinderSet.createFor(new ParameterDescription("string", TestBindable.class), new HttpPostDataMap(map)), is(nullValue()));

		parameterBinderSet.addBinder(new TestParameterBinder());
		Object bindo = parameterBinderSet.createFor(new ParameterDescription("string", TestBindable.class), new HttpPostDataMap(map));
		assertThat(bindo instanceof TestBindable, is(true));
		assertThat(bindo, is((Object) new TestBindable("value")));
	}

	@Test
	public void shouldAllowRegistrationOfParameterBinders() {
		registeredBinder = new TestParameterBinder();
		Map<String, String[]> map = map("bind", new String[] { "bound!" });
		{
			ParameterBinderSet parameterBinderSet = new ParameterBinderSet();
			assertThat(parameterBinderSet.createFor(new ParameterDescription("bind", TestBindable.class), new HttpPostDataMap(map)), is(nullValue()));
		}

		ParameterBinderSet.registerGlobalBinder(registeredBinder);
		{
			ParameterBinderSet parameterBinderSet = new ParameterBinderSet();
			assertThat(parameterBinderSet.createFor(new ParameterDescription("bind", TestBindable.class), new HttpPostDataMap(map)), is((Object) new TestBindable("bound!")));
		}
	}

	@Test
	public void shouldAllowUnregistrationOfParameterBinders() {
		registeredBinder = new TestParameterBinder();
		ParameterBinderSet.registerGlobalBinder(registeredBinder);
		ParameterBinderSet.unregisterGlobalBinder(registeredBinder);
		Map<String, String[]> map = map("bind", new String[] { "bound!" });
		ParameterBinderSet parameterBinderSet = new ParameterBinderSet();
		assertThat(parameterBinderSet.createFor(new ParameterDescription("bind", TestBindable.class), new HttpPostDataMap(map)), is(nullValue()));
	}

	@Test
	public void shouldNotFailOnMultipleUnregisterOfSameParameterBinder() {
		registeredBinder = new TestParameterBinder();
		ParameterBinderSet.registerGlobalBinder(registeredBinder);
		ParameterBinderSet.unregisterGlobalBinder(registeredBinder);
		ParameterBinderSet.unregisterGlobalBinder(registeredBinder);
	}

	@Test
	public void shouldNotFailOnUnregisterOfNonRegisteredParameterBinder() {
		registeredBinder = new TestParameterBinder();
		ParameterBinderSet.unregisterGlobalBinder(registeredBinder);
		ParameterBinderSet.unregisterGlobalBinder((ParameterBinder<?>) null);
	}

	private static class TestBindable {
		private String value;

		public TestBindable(String value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestBindable other = (TestBindable) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}

	private static class TestParameterBinder implements ParameterBinder<TestBindable> {
		@Override
		public boolean willBind(ParameterDescription parameterDescription) {
			return parameterDescription.isA(TestBindable.class);
		}

		@Override
		public TestBindable bind(ParameterBinderSet binders, ParameterDescription parameterDescription, HttpPostDataMap pathMap) {
			String[] strings = pathMap.get(parameterDescription.name());
			return new TestBindable(strings[0]);
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == null ? false : this.getClass() == obj.getClass();
		}
	}
}
