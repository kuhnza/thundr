/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://www.3wks.com.au/thundr
 * Copyright (C) 2013 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.thundr.route;

import static com.threewks.thundr.route.Route.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.expressive.Expressive;

public class RouteTest {
	@Rule public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldCorrectlyReplaceSingleAndDoubleAsteriskInPathString() {
		assertThat(Route.convertPathStringToRegex("/route/**"), isPath("/route/[%s]*?(?:;.*?)*", Route.AcceptableMultiPathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/*"), isPath("/route/[%s]*?(?:;.*?)*", Route.AcceptablePathCharacters));

		assertThat(Route.convertPathStringToRegex("*"), isPath("[%s]*?(?:;.*?)*", Route.AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("**"), isPath("[%s]*?(?:;.*?)*", Route.AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("/route/*/*/"), isPath("/route/[%s]*?/[%s]*?/(?:;.*?)*", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/*/**"), isPath("/route/[%s]*?/[%s]*?(?:;.*?)*", AcceptablePathCharacters, AcceptableMultiPathCharacters));
		assertThat(Route.convertPathStringToRegex("/route/**/**"), isPath("/route/[%s]*?/[%s]*?(?:;.*?)*", AcceptableMultiPathCharacters, AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("**/info"), isPath("[%s]*?/info(?:;.*?)*", AcceptableMultiPathCharacters));

		assertThat(Route.convertPathStringToRegex("**.png"), isPath("[%s]*?.png(?:;.*?)*", AcceptableMultiPathCharacters));
	}

	@Test
	public void shouldCorrectlyReplaceVariablesInPathString() {
		assertThat(Route.convertPathStringToRegex("/something/{var}"), isPath("/something/([%s]+)(?:;.*?)*", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/{var2}"), isPath("/something/([%s]+)/([%s]+)(?:;.*?)*", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("{var}"), isPath("([%s]+)(?:;.*?)*", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("{var}/something/"), isPath("([%s]+)/something/(?:;.*?)*", AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/more/{var2}"), isPath("/something/([%s]+)/more/([%s]+)(?:;.*?)*", AcceptablePathCharacters, AcceptablePathCharacters));
		assertThat(Route.convertPathStringToRegex("/something/{var}/more/{var2}.jpg"), isPath("/something/([%s]+)/more/([%s]+).jpg(?:;.*?)*", AcceptablePathCharacters, AcceptablePathCharacters));
	}

	@Test
	public void shouldMatchGivenPaths() {
		assertThat(new Route(null, "/path/{var}/{var2}", null).matches("/path/result1/result2"), is(true));
		assertThat(new Route(null, "/something/{var}/more/{var2}", null).matches("/something/123/more/1234"), is(true));
		assertThat(new Route(null, "/something/{var}/more/{var2}.jpg", null).matches("/something/123/more/1234.jpg"), is(true));
		assertThat(new Route(null, "/something/file.{ext}", null).matches("/something/file.gif"), is(true));
		// appending query parameters using semi-colon is within the http rfc spec, although weird
		// some versions of jetty and tomcat do this on redirects, for example
		assertThat(new Route(null, "/path/{var}/{var2}", null).matches("/path/result1/result2;jsessionid=ASD123-123DAFa"), is(true));
		assertThat(new Route(null, "/path/{var}/{var2}", null).matches("/path/result1/result2;jsessionid=ASD123-123DAFa;other=some%20value"), is(true));
	}

	@Test
	public void shouldMatchPathsWithEscapedCharacters() {
		assertThat(new Route(null, "/something/{var}/{var2}", null).matches("/something/Here%2C%20be%20/dragons%20"), is(true));
		assertThat(new Route(null, "/something/{var}/{var2}", null).matches("/something/Here-be/dragons"), is(true));
		assertThat(new Route(null, "/something/{var}/{var2}/", null).matches("/something/Here%2C%20be%20/dragons/"), is(true));
		assertThat(new Route(null, "/something/{var}/{var2}/", null).matches("/something/Here-be/dragons/"), is(true));

		assertThat(new Route(null, "/browse/{category}/", null).matches("/browse/Beauty%2C%20Health%20%26%20Wellbeing/"), is(true));
		assertThat(new Route(null, "/browse/{category}/", null).matches("/browse/Beauty,%20Health%20%26%20Wellbeing/"), is(true));
	}

	@Test
	public void shouldMatchGivenWildcardPaths() {
		assertThat(new Route(null, "/path/", null).matches("/path/"), is(true));
		assertThat(new Route(null, "/path/", null).matches("/path/more"), is(false));
		assertThat(new Route(null, "/path/", null).matches("/path"), is(false));
		assertThat(new Route(null, "/path/", null).matches("path/"), is(false));
		assertThat(new Route(null, "/path/", null).matches("/things/path/"), is(false));

		assertThat(new Route(null, "/file.ext", null).matches("/file.ext"), is(true));
		assertThat(new Route(null, "/path/file.ext", null).matches("/path/file.ext"), is(true));

		assertThat(new Route(null, "/path/*", null).matches("/path/"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more/"), is(false));
		assertThat(new Route(null, "/path/*", null).matches("/path"), is(false));
		assertThat(new Route(null, "/path/*", null).matches("/path/resource/1/is/here"), is(false));
		assertThat(new Route(null, "/path/*", null).matches("/path/file.ext"), is(true));
		assertThat(new Route(null, "/path/file.*", null).matches("/path/file.ext"), is(true));
		assertThat(new Route(null, "/path/file.*", null).matches("/path/file."), is(true));
		assertThat(new Route(null, "/path/file*", null).matches("/path/file"), is(true));

		assertThat(new Route(null, "/path/**", null).matches("/path/resource/1/is/here"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/resource/1/is/here/"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path"), is(false));
		assertThat(new Route(null, "/path/**", null).matches("/path/file.ext"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/1/2/3/more/file.ext"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/1/2/3/more/file."), is(true));
	}

	@Test
	public void shouldMatchGivenWildcardPathsWithEscapedUrls() {
		assertThat(new Route(null, "/path/*", null).matches("/path/"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more+val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more%20val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more_val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more!val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more$val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more,val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more'val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more.val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more(val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more)val"), is(true));
		assertThat(new Route(null, "/path/*", null).matches("/path/more*val"), is(true));

		assertThat(new Route(null, "/path/**", null).matches("/path/more+val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more%20val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more_val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more!val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more$val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more,val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more'val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more.val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more(val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more)val/a"), is(true));
		assertThat(new Route(null, "/path/**", null).matches("/path/more*val/a"), is(true));
	}

	@Test
	public void shouldNotMatchPathWhereTheVariableWouldCoverTwoValues() {
		assertThat(new Route(null, "/something/{var}/more/{var2}", null).matches("/something/123/more/1234/5678"), is(false));
	}

	@Test
	public void shouldExtractPathVariablesFromRoute() {
		assertThat(new Route(null, "/path/{var}/{var2}", null).getPathVars("/path/result1/result2"), is(Expressive.<String, String> map("var", "result1", "var2", "result2")));
		assertThat(new Route(null, "/something/{var}/more/{var2}", null).getPathVars("/something/123/more/1234"), is(Expressive.<String, String> map("var", "123", "var2", "1234")));
		assertThat(new Route(null, "/something/{var}/more/{var2}", null).getPathVars("/something/123/more/1234/5678"), is(Expressive.<String, String> map("var", "123", "var2", "1234")));
	}

	@Test
	public void shouldReturnRoute() {
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getRoute(), is("/path/{var}/split/{var2}"));
	}

	@Test
	public void shouldReturnReverseRoute() {
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getReverseRoute(map("var", "value", "var2", 1)), is("/path/value/split/1"));
		assertThat(route.getReverseRoute(map("var", "key", "var2", new DateTime(2000, 1, 1, 0, 0).withZoneRetainFields(DateTimeZone.UTC))), is("/path/key/split/2000-01-01T00%3A00%3A00.000Z"));
	}

	@Test
	public void shouldReturnReverseRouteEncodingStringAsPathComponent() {
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getReverseRoute(map("var", "path=string", "var2", "another value")), is("/path/path%3Dstring/split/another%20value"));
	}

	@Test
	public void shouldReturnReverseRouteWhenNoParametersRequired() {
		Route route = new Route(null, "/path/to/resource.html", null);
		assertThat(route.getReverseRoute(map()), is("/path/to/resource.html"));
	}

	@Test
	public void shouldThrowReverseRouteExceptionIfTooFewArgumentsSupplied() {
		thrown.expect(ReverseRouteException.class);
		thrown.expectMessage("Cannot generate a reverse route for /path/{var}/split/{var2} - no value(s) supplied for the path variables var2");
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getReverseRoute(map("var", "value")), is("/path/value/split/"));
	}

	@Test
	public void shouldGenerateReverseRouteIgnoringExtraParameters() {
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getReverseRoute(map("var", "value", "var2", 1, "var3", 2)), is("/path/value/split/1"));
	}

	@Test
	public void shouldThrowReverseRouteExceptionIfContainsNullArgumentsSupplied() {
		thrown.expect(ReverseRouteException.class);
		thrown.expectMessage("Cannot generate a reverse route for /path/{var}/split/{var2} - one or more parameters were null");
		Route route = new Route(null, "/path/{var}/split/{var2}", null);
		assertThat(route.getReverseRoute(map("var", "value", "var2", null)), is("/path/value/split/"));

	}

	private Matcher<String> isPath(String format, Object... args) {
		return Matchers.is(String.format(format, args));
	}

	private Map<String, Object> map(Object... args) {
		return Expressive.map(args);
	}
}
