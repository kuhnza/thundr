package com.threewks.thundr.action.method.bind;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BindExceptionTest {

	@Test
	public void shouldHaveAMessageCtor() {
		BindException e = new BindException("Message: %s", "expected");
		assertThat(e.getCause(), is(nullValue()));
		assertThat(e.getMessage(), is("Message: expected"));
	}

	@Test
	public void shouldHaveCauseAndMessageCtor() {
		Exception cause = new RuntimeException();
		BindException e = new BindException(cause, "Message: %s", "expected");
		assertThat(e.getCause(), is((Throwable) cause));
		assertThat(e.getMessage(), is("Message: expected"));
	}
}
