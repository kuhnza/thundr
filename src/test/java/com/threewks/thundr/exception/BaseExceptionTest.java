package com.threewks.thundr.exception;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BaseExceptionTest {

	@Test
	public void shouldHaveDefaultCtorForLazyPeople() throws InstantiationException, IllegalAccessException {
		assertThat(BaseException.class.newInstance(), is(notNullValue()));
		assertThat(BaseException.class.newInstance().getMessage(), is(nullValue()));
		assertThat(BaseException.class.newInstance().getCause(), is(nullValue()));
	}

	@Test
	public void shouldRetainFormattedMessage() {
		assertThat(new BaseException("%s", "message").getMessage(), is("message"));
		assertThat(new BaseException("%s", "message").getCause(), is(nullValue()));
	}

	@Test
	public void shouldRetainFormattedMessageAndCause() {
		Throwable cause = new Throwable();
		assertThat(new BaseException(cause, "%s", "message").getMessage(), is("message"));
		assertThat(new BaseException(cause, "%s", "message").getCause(), is(cause));
	}

	@Test
	public void shouldRetainCause() {
		Throwable cause = new Throwable();
		assertThat(new BaseException(cause).getCause(), is(cause));
		assertThat(new BaseException(cause).getMessage(), is("java.lang.Throwable"));
	}
}
