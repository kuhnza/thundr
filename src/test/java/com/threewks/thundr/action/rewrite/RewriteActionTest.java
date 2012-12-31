package com.threewks.thundr.action.rewrite;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class RewriteActionTest {
	private Map<String, String> emptyPathVars = Collections.emptyMap();

	@Test
	public void shouldCreateSimpleRedirect() {
		RewriteAction redirectAction = new RewriteAction("/path/to/file.html");
		assertThat(redirectAction.getRewriteTo(emptyPathVars), is("/path/to/file.html"));
	}

	@Test
	public void shouldRedirectWithPathVar() {
		Map<String, String> pathVars = map("to", "destination");
		RewriteAction redirectAction = new RewriteAction("/path/{to}/file.html");
		assertThat(redirectAction.getRewriteTo(pathVars), is("/path/destination/file.html"));
	}

	@Test
	public void shouldRedirectWithMissingPathVar() {
		Map<String, String> pathVars = map("to", "destination");
		RewriteAction redirectAction = new RewriteAction("/path/{to}/{file}.html");
		assertThat(redirectAction.getRewriteTo(pathVars), is("/path/destination/.html"));
	}

	@Test
	public void shouldRedirectWithExtraPathVars() {
		Map<String, String> pathVars = map("to", "destination", "other", "whatever");
		RewriteAction redirectAction = new RewriteAction("/path/{to}/other");
		assertThat(redirectAction.getRewriteTo(pathVars), is("/path/destination/other"));
	}

	@Test
	public void shouldHaveUsefulToString() {
		assertThat(new RewriteAction("/path/{to}").toString(), is("Rewrite:/path/{to}"));
	}
}
