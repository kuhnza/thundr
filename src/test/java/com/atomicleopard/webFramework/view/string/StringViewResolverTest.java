package com.atomicleopard.webFramework.view.string;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletRequest;
import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletResponse;
import com.atomicleopard.webFramework.view.ViewResolutionException;

public class StringViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	private StringViewResolver stringViewResolver = new StringViewResolver();
	private HttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();

	@Test
	public void shouldWriteViewToResponse() {
		stringViewResolver.resolve(req, resp, new StringView("My view result"));
		assertThat(resp.content(), is("My view result"));
		assertThat(resp.isCommitted(), is(true));
	}

	@Test
	public void shouldThrowViewResolutionExceptionIfWriteFails() throws IOException {
		thrown.expect(ViewResolutionException.class);
		resp = spy(resp);
		when(resp.getWriter()).thenThrow(new IOException("simulated exception"));
		stringViewResolver.resolve(req, resp, new StringView("My view result"));
	}
}
