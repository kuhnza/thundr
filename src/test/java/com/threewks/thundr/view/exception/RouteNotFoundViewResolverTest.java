package com.threewks.thundr.view.exception;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.route.RouteNotFoundException;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class RouteNotFoundViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private HttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private RouteNotFoundException viewResult = new RouteNotFoundException("");
	private RouteNotFoundViewResolver resolver = new RouteNotFoundViewResolver();

	@Test
	public void shouldSendError404() {
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(404));
	}

	@Test
	public void shouldThrowViewResolutionExceptionIfSendingErrorFails() {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to send error status 404");
		MockHttpServletResponse resp = new MockHttpServletResponse() {
			@Override
			public void sendError(int sc) throws IOException {
				throw new IOException("expected");
			}
		};
		resolver.resolve(req, resp, viewResult);
	}
}
