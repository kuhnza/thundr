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
package com.threewks.thundr.injection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.configuration.Environment;

public class InjectionContextImplTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private InjectionContextImpl context = new InjectionContextImpl();

	@Before
	public void before() {
		Environment.set(Environment.DEV);
	}

	@After
	public void after() {
		Environment.set(null);
	}

	@Test
	public void shouldInjectUsingInstance() {
		context.inject("String").as(String.class);
		assertThat(context.get(String.class, "value1"), is("String"));
	}

	@Test
	public void shouldInjectUsingNamedInstance() {
		context.inject("String").named("value1").as(String.class);
		context.inject("Another String").named("value2").as(String.class);
		context.inject("One More String").as(String.class);
		assertThat(context.get(String.class, "value1"), is("String"));
		assertThat(context.get(String.class, "value2"), is("Another String"));
		assertThat(context.get(String.class, "value3"), is("One More String"));
	}

	@Test
	public void shouldAllowInjectionOfInstanceByType() {
		Date date = new Date();
		context.inject("actual string").as(String.class);
		context.inject(date).as(Date.class);

		assertThat(context.get(String.class), is("actual string"));
		assertThat(context.get(Date.class), sameInstance(date));
	}

	@Test
	public void shouldAllowInjectionOfInstanceAsNamedType() {
		Date firstDate = new Date();
		Date secondDate = new Date();
		context.inject("first string").named("firstString").as(String.class);
		context.inject(firstDate).named("firstDate").as(Date.class);
		context.inject("second string").named("secondString").as(String.class);
		context.inject(secondDate).named("secondDate").as(Date.class);

		assertThat(context.get(String.class, "firstString"), is("first string"));
		assertThat(context.get(String.class, "secondString"), is("second string"));
		assertThat(context.get(Date.class, "firstDate"), sameInstance(firstDate));
		assertThat(context.get(Date.class, "secondDate"), sameInstance(secondDate));
	}

	@Test
	public void shouldReturnSameInstanceForTypeAfterFirstGet() {
		context.inject(Date.class).as(Date.class);
		Date firstDate = context.get(Date.class);
		Date secondDate = context.get(Date.class);
		assertThat(firstDate, is(sameInstance(secondDate)));
	}

	@Test
	public void shouldReturnSameInstanceForTypeAfterFirstGetNamedInstanceByType() {
		context.inject(Date.class).named("date").as(Date.class);
		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");
		assertThat(firstDate, is(sameInstance(secondDate)));
	}

	@Test
	public void shouldReturnUnnamedTypeIfNamedTypeNotPresent() {
		context.inject(Date.class).as(Date.class);
		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");

		assertThat(firstDate, is(notNullValue()));
		assertThat(secondDate, is(notNullValue()));
		assertThat(firstDate, is(sameInstance(secondDate)));
	}

	@Test
	public void shouldReturnUnnamedInstanceIfNamedInstanceNotPresent() {
		Date date = new Date();
		context.inject(date).as(Date.class);

		// unnamed instance is preferred over unnamed type
		context.inject(Date.class).as(Date.class);

		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");

		assertThat(firstDate, is(notNullValue()));
		assertThat(secondDate, is(notNullValue()));
		assertThat(firstDate, sameInstance(date));
		assertThat(firstDate, sameInstance(secondDate));
	}

	@Test
	public void shouldReturnNamedTypeIfNamedInstanceNotPresent() {
		context.inject(Date.class).named("date").as(Date.class);
		// unnamed instance, should be less preferrable to a named type
		Date other = new Date();
		context.inject(other).as(Date.class);

		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");

		assertThat(firstDate, is(notNullValue()));
		assertThat(secondDate, is(notNullValue()));
		assertThat(firstDate, not(sameInstance(other)));
		assertThat(firstDate, sameInstance(secondDate));
	}

	@Test
	public void shouldReturnNamedInstanceIfUnnamedInstanceNotPresent() {
		Date date = new Date();
		context.inject("someString").named("someString").as(String.class);
		context.inject(0).named("someInt").as(int.class);
		
		context.inject(date).named("first").as(Date.class);
		// should prefer a named instance over a named type
		context.inject(Date.class).named("first").as(Date.class);

		Date firstDate = context.get(Date.class);

		assertThat(firstDate, is(notNullValue()));
		assertThat(firstDate, sameInstance(date));
	}
	
	@Test
	public void shouldReturnUnnamedInstanceIfUnnamedTypeNotPresent() {
		Date date = new Date();
		context.inject(date).as(Date.class);

		Date firstDate = context.get(Date.class);

		assertThat(firstDate, is(notNullValue()));
		assertThat(firstDate, sameInstance(date));
	}

	@Test
	public void shouldReturnNamedInstanceIfUnnamedTypeNotPresent() {
		Date date = new Date();
		context.inject(date).named("first").as(Date.class);

		Date firstDate = context.get(Date.class);

		assertThat(firstDate, is(notNullValue()));
		assertThat(firstDate, sameInstance(date));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldNotReturnADifferentNamedInstanceForBasicTypes() {
		List<Class> types = Expressive.<Class> list(String.class, int.class, Integer.class, short.class, Short.class, long.class, Long.class, float.class, Float.class, double.class, Double.class,
				byte.class, Byte.class, char.class, Character.class, List.class, Set.class, Map.class, Collection.class);
		for (Class type : types) {
			String name = type.getSimpleName();
			context.inject(type).named(name).as(type);
			assertThat(context.get(type), is(nullValue()));
			assertThat(context.contains(type), is(false));
			assertThat(context.contains(type, name), is(true));
		}
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidTypeEntry() {
		assertThat(context.contains(Date.class), is(false));
		context.inject(Date.class).named("date").as(Date.class);

		assertThat(context.contains(Date.class), is(false));
		context.inject(Date.class).as(Date.class);

		assertThat(context.contains(Date.class), is(true));
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidInstanceEntry() {
		assertThat(context.contains(Date.class), is(false));
		Date date = new Date();
		context.inject(date).named("date").as(Date.class);

		assertThat(context.contains(Date.class), is(false));
		context.inject(date).as(Date.class);

		assertThat(context.contains(Date.class), is(true));
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidNamedTypeEntry() {
		assertThat(context.contains(Date.class), is(false));
		assertThat(context.contains(Date.class, "date"), is(false));

		context.inject(Date.class).named("some other date").as(Date.class);

		assertThat(context.contains(Date.class, "date"), is(false));

		context.inject(Date.class).named("date").as(Date.class);
		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidNamedInstanceEntry() {
		assertThat(context.contains(Date.class), is(false));
		assertThat(context.contains(Date.class, "date"), is(false));
		Date date = new Date();

		context.inject(date).named("some other date").as(Date.class);

		assertThat(context.contains(Date.class, "date"), is(false));

		context.inject(date).named("date").as(Date.class);
		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidEnvironmentSpecificNamedInstanceEntry() {
		Date date = new Date();
		context.inject(date).named("date%dev").as(Date.class);
		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnTrueWhenContainsAValidEnvironmentNamedTypeEntry() {
		context.inject(Date.class).named("date%dev").as(Date.class);
		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnTrueWhenRequestingANamedTypeButOnlyAnUnamedTypeExists() {
		context.inject(Date.class).as(Date.class);

		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnNullWhenGetNonExistantInstance() {
		assertThat(context.get(BigDecimal.class), is(nullValue()));
		assertThat(context.get(BigDecimal.class, "name"), is(nullValue()));
	}

	@Test
	public void shouldInstantiateANewTypeProvidingValuesBasedOnTheMostSpecificConstructorThatCanBeInjected() {
		context.inject("arg1 value").named("arg1").as(String.class);
		context.inject("arg2 value").named("arg2").as(String.class);
		context.inject("injected value").named("injectedArg").as(String.class);
		context.inject(TestClass.class).as(TestClass.class);

		assertThat(context.contains(TestClass.class), is(true));
		TestClass testClass = context.get(TestClass.class);
		// this ensures the longest ctor was invoked
		assertThat(testClass.getArg1(), is("arg1 value"));
		assertThat(testClass.getArg2(), is("arg2 value"));
	}

	@Test
	public void shouldInvokeZeroArgConstructor() {
		context.inject("injected value").named("injectedArg").as(String.class);
		context.inject(TestClass.class).as(TestClass.class);
		assertThat(context.contains(TestClass.class), is(true));
		TestClass testClass = context.get(TestClass.class);
		assertThat(testClass.getInjectedArg(), is("injected value"));
		assertThat(testClass.getArg1(), is(nullValue()));
		assertThat(testClass.getArg2(), is(nullValue()));
		assertThat(testClass.getConstructorCalled(), is(0));
	}

	@Test
	public void shouldInvokeOneArgConstructor() {
		context.inject("injected value").named("injectedArg").as(String.class);
		context.inject(TestClass.class).as(TestClass.class);
		context.inject("arg1 value").named("arg1").as(String.class);
		TestClass testClass = context.get(TestClass.class);
		assertThat(testClass.getInjectedArg(), is("injected value"));
		assertThat(testClass.getArg1(), is("arg1 value"));
		assertThat(testClass.getArg2(), is(nullValue()));
		assertThat(testClass.getConstructorCalled(), is(1));

	}

	@Test
	public void shouldInvokeTwoArgConstructor() {
		context.inject("injected value").named("injectedArg").as(String.class);
		context.inject(TestClass.class).as(TestClass.class);
		context.inject("arg1 value").named("arg1").as(String.class);
		context.inject("arg2 value").named("arg2").as(String.class);
		TestClass testClass = context.get(TestClass.class);
		assertThat(testClass.getInjectedArg(), is("injected value"));
		assertThat(testClass.getArg1(), is("arg1 value"));
		assertThat(testClass.getArg2(), is("arg2 value"));
		assertThat(testClass.getConstructorCalled(), is(2));
	}

	@Test
	public void shouldSetFieldsWithSettersAndInjectInjectableFields() {
		context.inject("set arg value").named("settableArg").as(String.class);
		context.inject("injected value").named("injectedArg").as(String.class);
		context.inject(TestClass.class).as(TestClass.class);

		assertThat(context.contains(TestClass.class), is(true));
		TestClass testClass = context.get(TestClass.class);
		// this ensures the longest ctor was invoked
		assertThat(testClass.getSettableArg(), is("set arg value"));
		assertThat(testClass.getInjectedArg(), is("injected value"));
	}

	@Test
	public void shouldFailedToCreateInstanceWhenUnableToSatisfyCtor() {
		thrown.expect(InjectionException.class);
		thrown.expectMessage("Could not create a com.threewks.thundr.injection.TestClass2 - cannot match parameters of any available constructors");

		context.inject(TestClass2.class).as(TestClass2.class);
		assertThat(context.contains(TestClass2.class), is(true));

		context.get(TestClass2.class);
	}
	
	@Test
	public void shouldThrowInjectionExceptionWhenConstructionOfTypeFails() {
		thrown.expect(InjectionException.class);
		thrown.expectMessage("Failed to create a new instance using the constructor public com.threewks.thundr.injection.TestClass2(boolean): expected");

		context.inject(true).named("throwsException").as(boolean.class);
		context.inject(TestClass2.class).as(TestClass2.class);
		assertThat(context.contains(TestClass2.class), is(true));

		context.get(TestClass2.class);
	}
	
	@Test
	public void shouldThrowInjectionExceptionWhenSettingFieldOnTypeFails() {
		thrown.expect(InjectionException.class);
		thrown.expectMessage("Failed to inject into com.threewks.thundr.injection.TestClass2.setArg2: expected");

		context.inject("arg1").named("arg1").as(String.class);
		context.inject(true).named("arg2").as(boolean.class);
		context.inject(TestClass2.class).as(TestClass2.class);
		assertThat(context.contains(TestClass2.class), is(true));

		context.get(TestClass2.class);
	}

	@Test
	public void shouldGetEnvironmentSpecificValueWherePossible() {
		context.inject("defaultvalue").named("key").as(String.class);
		context.inject("value").named("key%dev").as(String.class);
		context.inject("prodvalue").named("key%prod").as(String.class);
		assertThat(context.get(String.class, "key"), is("value"));
		assertThat(context.get(String.class, "key%dev"), is("value"));
		Environment.set("prod");
		assertThat(context.get(String.class, "key"), is("prodvalue"));
		assertThat(context.get(String.class, "key%dev"), is("value"));
	}

	@Test
	public void shouldReturnTrueForContainsWhenEnvironmentSpecificValue() {
		assertThat(context.contains(String.class, "key"), is(false));

		context.inject("value").named("key%dev").as(String.class);
		assertThat(context.contains(String.class, "key"), is(true));

		Environment.set("prod");
		assertThat(context.contains(String.class, "key"), is(false));

		context.inject("defaultvalue").named("key").as(String.class);
		assertThat(context.contains(String.class, "key"), is(true));
	}
}
