package com.atomicleopard.webFramework.action.rewrite;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.atomicleopard.webFramework.action.rewrite.RewriteAction;
import com.atomicleopard.webFramework.action.rewrite.RewriteActionResolver;
import com.atomicleopard.webFramework.route.RouteType;
import com.atomicleopard.webFramework.route.Routes;

public class RewriteActionResolverTest {
	private RewriteActionResolver resolver;
	private Routes routes;

	@Before
	public void before() {
		routes = mock(Routes.class);
		resolver = new RewriteActionResolver(routes);
	}

	@Test
	public void shouldResolveRewriteAction() {
		Map<String, String> pathVars = map("to", "to", "else", "else");
		RewriteAction action = resolver.createActionIfPossible("rewrite:/something/{to}/something/{else}/");
		assertThat(action, is(notNullValue()));
		assertThat(action.getRewriteTo(pathVars), is("/something/to/something/else/"));
	}
	
	@Test
	public void shouldResolveRewriteActionRegardlessOfCase() {
		Map<String, String> pathVars = map("to", "to", "else", "else");
		RewriteAction action = resolver.createActionIfPossible("reWRIte:/something/{to}/something/{else}/");
		assertThat(action, is(notNullValue()));
		assertThat(action.getRewriteTo(pathVars), is("/something/to/something/else/"));
	}

	@Test
	public void shouldNotResolveRewriteAction() {
		RewriteAction action = resolver.createActionIfPossible("somethingelse:/something/{to}/something/{else}/");
		assertThat(action, is(nullValue()));
	}

	@Test
	public void shouldInvokeRoutesToPerformRewriteAction() {
		RewriteAction action = new RewriteAction("/rewrite/{to}");
		RouteType routeType = RouteType.POST;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		Map<String, String> pathVars = map("to", "new");
		resolver.resolve(action, routeType, req, resp, pathVars);

		verify(routes).invoke("/rewrite/new", routeType, req, resp);
	}
}
