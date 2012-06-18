package com.threewks.thundr.view.json;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class JsonViewResolverTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private JsonViewResolver resolver = new JsonViewResolver();

	@Test
	public void shouldResolveByWritingJsonToOutputStream() throws IOException {
		JsonView viewResult = new JsonView(map("key", "value"));
		resolver.resolve(req, resp, viewResult);
		assertThat(resp.status(), is(HttpServletResponse.SC_OK));
		assertThat(resp.content(), is("{\"key\":\"value\"}"));
		assertThat(resp.getContentLength(), is(15));
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenFailedToWriteJsonToOutputStream() throws IOException {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to generate JSON output for object 'string'");

		resp = spy(resp);
		when(resp.getWriter()).thenThrow(new RuntimeException("fail"));
		JsonView viewResult = new JsonView("string");
		resolver.resolve(req, resp, viewResult);
	}
}
