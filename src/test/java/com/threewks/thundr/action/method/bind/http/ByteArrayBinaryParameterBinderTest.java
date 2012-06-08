package com.threewks.thundr.action.method.bind.http;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.threewks.thundr.introspection.ParameterDescription;

public class ByteArrayBinaryParameterBinderTest {
	private ByteArrayBinaryParameterBinder binder = new ByteArrayBinaryParameterBinder();

	@Test
	public void shouldReturnTrueForWillBindOnByteArrays() {
		assertThat(binder.willBind(new ParameterDescription("data", byte[].class)), is(true));
		assertThat(binder.willBind(new ParameterDescription("data", int[].class)), is(false));
		assertThat(binder.willBind(new ParameterDescription("data", byte.class)), is(false));
	}

	@Test
	public void shouldBindByteArrayByReturningByteArray() {
		assertThat(binder.bind(new ParameterDescription("data", byte[].class), new byte[] { 1, 2, 3 }), is(new byte[] { 1, 2, 3 }));
	}
}
