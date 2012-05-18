package com.threewks.thundr.route;

import static com.threewks.thundr.route.Route.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.atomicleopard.expressive.Expressive;

public class RouteTest {
	@Test
	public void shouldCorrectlyReplaceSingleAndDoubleAsteriskInPathString() {
		assertThat(Route.convertPathStringToRegex("/route/**"), isPath("/route/[%s]*?", Route.AcceptableMultiPathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/*"), isPath("/route/[%s]*?", Route.AcceptablePathCharacters));

		assertThat(Route.convertPathStringToRegex("*"), isPath("[%s]*?", Route.AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("**"), isPath("[%s]*?", Route.AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("/route/*/*/"), isPath("/route/[%s]*?/[%s]*?/", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/*/**"), isPath("/route/[%s]*?/[%s]*?", AcceptablePathCharacters, AcceptableMultiPathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/**/**"), isPath("/route/[%s]*?/[%s]*?", AcceptableMultiPathCharacters, AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("**/info"), isPath("[%s]*?/info", AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("**.png"), isPath("[%s]*?.png", AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldCorrectlyReplaceVariablesInPathString() {
		assertThat(Route.convertPathStringToRegex("/something/{var}"), isPath("/something/([%s]+)", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/{var2}"), isPath("/something/([%s]+)/([%s]+)", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("{var}"), isPath("([%s]+)", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("{var}/something/"), isPath("([%s]+)/something/", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/more/{var2}"), isPath("/something/([%s]+)/more/([%s]+)", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/more/{var2}.jpg"), isPath("/something/([%s]+)/more/([%s]+).jpg", AcceptablePathCharacters, AcceptablePathCharacters));
	}

	@Test
	public void shouldMatchGivenPaths() {
		assertThat(new Route("/path/{var}/{var2}", null, null).matches("/path/result1/result2"), is(true));
		assertThat(new Route("/something/{var}/more/{var2}", null, null).matches("/something/123/more/1234"), is(true));
		assertThat(new Route("/something/{var}/more/{var2}.jpg", null, null).matches("/something/123/more/1234.jpg"), is(true));
		assertThat(new Route("/something/file.{ext}", null, null).matches("/something/file.gif"), is(true));
	}

	@Test
	public void shouldMatchPathsWithEscapedCharacters() {
		assertThat(new Route("/something/{var}/{var2}", null, null).matches("/something/Here%2C%20be%20/dragons%20"), is(true));
		assertThat(new Route("/something/{var}/{var2}", null, null).matches("/something/Here-be/dragons"), is(true));
		assertThat(new Route("/something/{var}/{var2}/", null, null).matches("/something/Here%2C%20be%20/dragons/"), is(true));
		assertThat(new Route("/something/{var}/{var2}/", null, null).matches("/something/Here-be/dragons/"), is(true));

		assertThat(new Route("/browse/{category}/", null, null).matches("/browse/Beauty%2C%20Health%20%26%20Wellbeing/"), is(true));
		assertThat(new Route("/browse/{category}/", null, null).matches("/browse/Beauty,%20Health%20%26%20Wellbeing/"), is(true));
	}

	@Test
	public void shouldMatchGivenWildcardPaths() {
		assertThat(new Route("/path/", null, null).matches("/path/"), is(true));
		assertThat(new Route("/path/", null, null).matches("/path/more"), is(false));
		assertThat(new Route("/path/", null, null).matches("/path"), is(false));
		assertThat(new Route("/path/", null, null).matches("path/"), is(false));
		assertThat(new Route("/path/", null, null).matches("/things/path/"), is(false));

		assertThat(new Route("/file.ext", null, null).matches("/file.ext"), is(true));
		assertThat(new Route("/path/file.ext", null, null).matches("/path/file.ext"), is(true));

		assertThat(new Route("/path/*", null, null).matches("/path/"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more/"), is(false));
		assertThat(new Route("/path/*", null, null).matches("/path"), is(false));
		assertThat(new Route("/path/*", null, null).matches("/path/resource/1/is/here"), is(false));
		assertThat(new Route("/path/*", null, null).matches("/path/file.ext"), is(true));
		assertThat(new Route("/path/file.*", null, null).matches("/path/file.ext"), is(true));
		assertThat(new Route("/path/file.*", null, null).matches("/path/file."), is(true));
		assertThat(new Route("/path/file*", null, null).matches("/path/file"), is(true));

		assertThat(new Route("/path/**", null, null).matches("/path/resource/1/is/here"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/resource/1/is/here/"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path"), is(false));
		assertThat(new Route("/path/**", null, null).matches("/path/file.ext"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/1/2/3/more/file.ext"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/1/2/3/more/file."), is(true));
	}

	@Test
	public void shouldMatchGivenWildcardPathsWithEscapedUrls() {
		assertThat(new Route("/path/*", null, null).matches("/path/"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more+val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more%20val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more_val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more!val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more$val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more,val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more'val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more.val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more(val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more)val"), is(true));
		assertThat(new Route("/path/*", null, null).matches("/path/more*val"), is(true));

		assertThat(new Route("/path/**", null, null).matches("/path/more+val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more%20val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more_val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more!val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more$val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more,val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more'val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more.val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more(val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more)val/a"), is(true));
		assertThat(new Route("/path/**", null, null).matches("/path/more*val/a"), is(true));
	}

	@Test
	public void shouldNotMatchPathWhereTheVariableWouldCoverTwoValues() {
		assertThat(new Route("/something/{var}/more/{var2}", null, null).matches("/something/123/more/1234/5678"), is(false));
	}

	@Test
	public void shouldExtractPathVariablesFromRoute() {
		assertThat(new Route("/path/{var}/{var2}", null, null).getPathVars("/path/result1/result2"), is(Expressive.<String, String> map("var", "result1", "var2", "result2")));
		assertThat(new Route("/something/{var}/more/{var2}", null, null).getPathVars("/something/123/more/1234"), is(Expressive.<String, String> map("var", "123", "var2", "1234")));
		assertThat(new Route("/something/{var}/more/{var2}", null, null).getPathVars("/something/123/more/1234/5678"), is(Expressive.<String, String> map("var", "123", "var2", "1234")));
	}

	private Matcher<String> isPath(String format, Object... args) {
		return Matchers.is(String.format(format, args));
	}
}
