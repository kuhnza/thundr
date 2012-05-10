package com.threewks.thundr.view.exception;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class HttpStatusExceptionViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private HttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private HttpStatusException viewResult = new HttpStatusException(101, "message");
	private HttpStatusExceptionViewResolver resolver = new HttpStatusExceptionViewResolver();

	@Test
	public void shouldSendError404() {
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(101));
	}

	@Test
	public void shouldThrowViewResolutionExceptionIfSendingErrorFails() {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to send error status 101");
		MockHttpServletResponse resp = new MockHttpServletResponse() {
			@Override
			public void sendError(int sc) throws IOException {
				throw new IOException("expected");
			}
		};
		resolver.resolve(req, resp, viewResult);
	}
}
