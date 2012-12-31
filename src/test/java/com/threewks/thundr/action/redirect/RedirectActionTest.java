package com.threewks.thundr.action.redirect;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class RedirectActionTest {
	private Map<String, String> emptyPathVars = Collections.emptyMap();

	@Test
	public void shouldCreateSimpleRedirect() {
		RedirectAction redirectAction = new RedirectAction("/path/to/file.html");
		assertThat(redirectAction.getRedirectTo(emptyPathVars), is("/path/to/file.html"));
	}

	@Test
	public void shouldRedirectWithPathVar() {
		Map<String, String> pathVars = map("to", "destination");
		RedirectAction redirectAction = new RedirectAction("/path/{to}/file.html");
		assertThat(redirectAction.getRedirectTo(pathVars), is("/path/destination/file.html"));
	}

	@Test
	public void shouldRedirectWithMissingPathVar() {
		Map<String, String> pathVars = map("to", "destination");
		RedirectAction redirectAction = new RedirectAction("/path/{to}/{file}.html");
		assertThat(redirectAction.getRedirectTo(pathVars), is("/path/destination/.html"));
	}

	@Test
	public void shouldRedirectWithExtraPathVars() {
		Map<String, String> pathVars = map("to", "destination", "other", "whatever");
		RedirectAction redirectAction = new RedirectAction("http://google.com?q={to}");
		assertThat(redirectAction.getRedirectTo(pathVars), is("http://google.com?q=destination"));
	}

	@Test
	public void shouldHaveUsefulToString() {
		assertThat(new RedirectAction("http://google.com?q={to}").toString(), is("Redirect:http://google.com?q={to}"));
	}
}
