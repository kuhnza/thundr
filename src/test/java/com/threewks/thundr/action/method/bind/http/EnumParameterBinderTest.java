package com.threewks.thundr.action.method.bind.http;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.lang.annotation.RetentionPolicy;

import org.junit.Test;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.introspection.ParameterDescription;

public class EnumParameterBinderTest {
	private EnumParameterBinder binder = new EnumParameterBinder();

	@Test
	public void shouldReturnTrueForWillBindOnAllEnumTypes() {
		assertThat(binder.willBind(new ParameterDescription("name", TestEnum.class)), is(true));
		assertThat(binder.willBind(new ParameterDescription("name", RetentionPolicy.class)), is(true));
		assertThat(binder.willBind(new ParameterDescription("name", String.class)), is(false));
		assertThat(binder.willBind(new ParameterDescription("name", Object.class)), is(false));
	}

	@Test
	public void shouldBindEnumsFromStrings() {
		ParameterDescription parameterDescription = new ParameterDescription("name", TestEnum.class);
		assertThat(binder.bind(null, parameterDescription, data("name", "text")), is((Object) TestEnum.text));
		assertThat(binder.bind(null, parameterDescription, data("name", "camelCase")), is((Object) TestEnum.camelCase));
		assertThat(binder.bind(null, parameterDescription, data("name", "numeric1")), is((Object) TestEnum.numeric1));
		assertThat(binder.bind(null, parameterDescription, data("name", "Standard")), is((Object) TestEnum.Standard));
		assertThat(binder.bind(null, parameterDescription, data("name", "ALL_CAPS")), is((Object) TestEnum.ALL_CAPS));

		assertThat(binder.bind(null, parameterDescription, data("name", "junk")), is(nullValue()));
		assertThat(binder.bind(null, parameterDescription, data("name", "all_caps")), is(nullValue()));
		assertThat(binder.bind(null, parameterDescription, data("name", "CAMELCASE")), is(nullValue()));
		assertThat(binder.bind(null, parameterDescription, data("name", "STANDARD")), is(nullValue()));
	}

	private HttpPostDataMap data(String name, String value) {
		HttpPostDataMap pathMap = new HttpPostDataMap(Expressive.<String, String[]> map(name, new String[] { value }));
		return pathMap;
	}

	private enum TestEnum {
		text,
		camelCase,
		numeric1,
		Standard,
		ALL_CAPS;
	}
}
