package com.atomicleopard.webFramework.routes;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RedirectActionResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private RedirectActionResolver resolver = new RedirectActionResolver();
	private Map<String, String> emptyMap = map();

	@Test
	public void shouldCreateRedirectAction() {
		RedirectAction createActionIfPossible = resolver.createActionIfPossible("redirect:/back/to/you");
		assertThat(createActionIfPossible, is(notNullValue()));
		assertThat(createActionIfPossible.getRedirectTo(emptyMap), is("/back/to/you"));
	}

	@Test
	public void shouldCreateRedirectActionRegardlessOfCase() {
		RedirectAction createActionIfPossible = resolver.createActionIfPossible("RedirecT:/back/to/you");
		assertThat(createActionIfPossible, is(notNullValue()));
		assertThat(createActionIfPossible.getRedirectTo(emptyMap), is("/back/to/you"));
	}

	@Test
	public void shouldNotResolveRedirectAction() {
		RedirectAction createActionIfPossible = resolver.createActionIfPossible("other:/back/to/you");
		assertThat(createActionIfPossible, is(nullValue()));
	}

	@Test
	public void shouldSendRedirectToClient() throws IOException {
		RedirectAction action = new RedirectAction("/redirect/{to}");
		RouteType routeType = RouteType.POST;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		Map<String, String> pathVars = map("to", "new");
		resolver.resolve(action, routeType, req, resp, pathVars);

		verify(resp).sendRedirect("/redirect/new");
	}

	@Test
	public void shouldThrowActionExceptionWhenRedirectFails() throws IOException {
		thrown.expect(ActionException.class);
		thrown.expectMessage("Failed to redirect /requested/path to /redirect/new");

		RedirectAction action = new RedirectAction("/redirect/{to}");
		RouteType routeType = RouteType.POST;
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/requested/path");
		HttpServletResponse resp = mock(HttpServletResponse.class);
		Map<String, String> pathVars = map("to", "new");

		doThrow(new IOException("expected")).when(resp).sendRedirect(anyString());
		resolver.resolve(action, routeType, req, resp, pathVars);
	}
}
