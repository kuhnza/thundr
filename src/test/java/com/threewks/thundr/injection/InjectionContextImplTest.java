package com.threewks.thundr.injection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.exception.BaseException;

public class InjectionContextImplTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private InjectionContextImpl context = new InjectionContextImpl();

	@Test
	public void shouldInjectUsingInstance() {
		context.inject(String.class).as("String");
		assertThat(context.get(String.class, "value1"), is("String"));
	}

	@Test
	public void shouldInjectUsingNamedInstance() {
		context.inject(String.class).named("value1").as("String");
		context.inject(String.class).named("value2").as("Another String");
		context.inject(String.class).as("One More String");
		assertThat(context.get(String.class, "value1"), is("String"));
		assertThat(context.get(String.class, "value2"), is("Another String"));
		assertThat(context.get(String.class, "value3"), is("One More String"));
	}

	@Test
	public void shouldAllowInjectionOfInstanceByType() {
		Date date = new Date();
		context.inject(String.class).as("actual string");
		context.inject(Date.class).as(date);

		assertThat(context.get(String.class), is("actual string"));
		assertThat(context.get(Date.class), sameInstance(date));
	}

	@Test
	public void shouldAllowInjectionOfInstanceAsNamedType() {
		Date firstDate = new Date();
		Date secondDate = new Date();
		context.inject(String.class).named("firstString").as("first string");
		context.inject(Date.class).named("firstDate").as(firstDate);
		context.inject(String.class).named("secondString").as("second string");
		context.inject(Date.class).named("secondDate").as(secondDate);

		assertThat(context.get(String.class, "firstString"), is("first string"));
		assertThat(context.get(String.class, "secondString"), is("second string"));
		assertThat(context.get(Date.class, "firstDate"), sameInstance(firstDate));
		assertThat(context.get(Date.class, "secondDate"), sameInstance(secondDate));
	}

	@Test
	public void shouldAllowInjectionOfNewInstanceByType() {
		context.inject(Date.class).as(Date.class);
		Date firstDate = context.get(Date.class);
		Date secondDate = context.get(Date.class);
		assertThat(firstDate, is(not(sameInstance(secondDate))));
	}

	@Test
	public void shouldAllowInjectionOfNamedInstanceByType() {
		context.inject(Date.class).named("date").as(Date.class);
		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");
		assertThat(firstDate, is(not(sameInstance(secondDate))));
	}

	@Test
	public void shouldReturnUnnamedTypeIfNamedTypeNotPresent() {
		context.inject(Date.class).as(Date.class);
		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");

		assertThat(firstDate, is(notNullValue()));
		assertThat(secondDate, is(notNullValue()));
		assertThat(firstDate, is(not(sameInstance(secondDate))));
	}

	@Test
	public void shouldReturnUnnamedInstanceIfNamedInstanceNotPresent() {
		Date date = new Date();
		context.inject(Date.class).as(date);

		Date firstDate = context.get(Date.class, "date");
		Date secondDate = context.get(Date.class, "date");

		assertThat(firstDate, is(notNullValue()));
		assertThat(secondDate, is(notNullValue()));
		assertThat(firstDate, sameInstance(secondDate));
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
		context.inject(Date.class).named("date").as(date);

		assertThat(context.contains(Date.class), is(false));
		context.inject(Date.class).as(date);

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

		context.inject(Date.class).named("some other date").as(date);

		assertThat(context.contains(Date.class, "date"), is(false));

		context.inject(Date.class).named("date").as(date);
		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldReturnTrueWhenRequestingANamedTypeButOnlyAnUnamedTypeExists() {
		context.inject(Date.class).as(Date.class);

		assertThat(context.contains(Date.class, "date"), is(true));
	}

	@Test
	public void shouldThrowNPEWhenGetNonExistantInstance() {
		thrown.expect(NullPointerException.class);
		context.get(BigDecimal.class);
	}

	@Test
	public void shouldInstantiateANewTypeProvidingValuesBasedOnTheMostSpecificConstructorThatCanBeInjected() {
		context.inject(String.class).named("arg1").as("arg1 value");
		context.inject(String.class).named("arg2").as("arg2 value");
		context.inject(String.class).named("injectedArg").as("injected value");
		context.inject(TestClass.class).as(TestClass.class);

		assertThat(context.contains(TestClass.class), is(true));
		TestClass testClass = context.get(TestClass.class);
		// this ensures the longest ctor was invoked
		assertThat(testClass.getArg1(), is("arg1 value"));
		assertThat(testClass.getArg2(), is("arg2 value"));
	}

	@Test
	public void shouldTryEachConstructorFromMostToLeastArguments() {
		context.inject(String.class).named("injectedArg").as("injected value");
		context.inject(TestClass.class).as(TestClass.class);
		assertThat(context.contains(TestClass.class), is(true));
		{
			TestClass testClass = context.get(TestClass.class);
			assertThat(testClass.getInjectedArg(), is("injected value"));
			assertThat(testClass.getArg1(), is(nullValue()));
			assertThat(testClass.getArg2(), is(nullValue()));
		}

		context.inject(String.class).named("arg1").as("arg1 value");
		{
			TestClass testClass = context.get(TestClass.class);
			assertThat(testClass.getInjectedArg(), is("injected value"));
			assertThat(testClass.getArg1(), is("arg1 value"));
			assertThat(testClass.getArg2(), is(nullValue()));
		}

		context.inject(String.class).named("arg2").as("arg2 value");
		{
			TestClass testClass = context.get(TestClass.class);
			assertThat(testClass.getInjectedArg(), is("injected value"));
			assertThat(testClass.getArg1(), is("arg1 value"));
			assertThat(testClass.getArg2(), is("arg2 value"));
		}
	}

	@Test
	public void shouldSetFieldsWithSettersAndInjectInjectableFields() {
		context.inject(String.class).named("settableArg").as("set arg value");
		context.inject(String.class).named("injectedArg").as("injected value");
		context.inject(TestClass.class).as(TestClass.class);

		assertThat(context.contains(TestClass.class), is(true));
		TestClass testClass = context.get(TestClass.class);
		// this ensures the longest ctor was invoked
		assertThat(testClass.getSettableArg(), is("set arg value"));
		assertThat(testClass.getInjectedArg(), is("injected value"));
	}

	@Test
	public void shouldFailedToCreateInstanceWhenUnableToSatisfyCtor() {
		thrown.expect(BaseException.class);
		context.inject(TestClass.class).as(TestClass.class);

		assertThat(context.contains(TestClass.class), is(true));
		context.get(TestClass.class);
	}

	@Test
	public void shouldInjectInstanceUsingType() {
		Date date = new Date();
		Date date2 = context.inject(date);
		assertThat(date2, sameInstance(date));
		assertThat(context.get(Date.class), sameInstance(date));
	}
}
