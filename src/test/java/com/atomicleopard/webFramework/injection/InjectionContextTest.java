package com.atomicleopard.webFramework.injection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InjectionContextTest {
	private InjectionContextImpl injector = new InjectionContextImpl();

	@Test
	public void shouldInjectUsingInstance() {
		injector.inject(String.class).as("String");
		assertThat(injector.get(String.class, "value1"), is("String"));
	}

	@Test
	public void shouldInjectUsingNamedInstance() {
		injector.inject(String.class).named("value1").as("String");
		injector.inject(String.class).named("value2").as("Another String");
		injector.inject(String.class).as("One More String");
		assertThat(injector.get(String.class, "value1"), is("String"));
		assertThat(injector.get(String.class, "value2"), is("Another String"));
		assertThat(injector.get(String.class, "value3"), is("One More String"));
	}
}
