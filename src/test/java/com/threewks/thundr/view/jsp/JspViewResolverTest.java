package com.threewks.thundr.view.jsp;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.test.mock.servlet.MockHttpSession;
import com.threewks.thundr.view.ViewResolutionException;

public class JspViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private JspViewResolver resolver = new JspViewResolver();
	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private ServletContext servletContext = mock(ServletContext.class);
	private MockHttpSession session = new MockHttpSession(servletContext);

	@Before
	public void before() throws MalformedURLException {
		URL url = new URL("file://file.jsp");
		when(servletContext.getResource(anyString())).thenReturn(url);
		req.session(session);
	}

	@Test
	public void shouldIncludeRequiredJspPage() {
		resolver.resolve(req, resp, new JspView("view.jsp"));
		assertThat(req.requestDispatcher().lastPath(), is("/WEB-INF/jsp/view.jsp"));
		assertThat(req.requestDispatcher().included(), is(true));
	}

	@Test
	public void shouldAddAllModelAttributesAsRequestAttributes() {
		Map<String, Object> model = mapKeys("attribute1", "attribute2").to("String val", list("Other", "Stuff"));
		resolver.resolve(req, resp, new JspView("view.jsp", model));
		assertThat(req.getAttribute("attribute1"), is((Object) "String val"));
		assertThat(req.getAttribute("attribute2"), is((Object) list("Other", "Stuff")));
	}

	@Test
	public void shouldAddAllGlobalModelAttributesAsRequestAttributes() {
		resolver.addToGlobalModel("key 1", "value 1");
		resolver.addAllToGlobalModel(Expressive.<String, Object> map("key 2", "value 2", "key 3", "value 3"));
		resolver.resolve(req, resp, new JspView("view.jsp", Expressive.<String, Object> map()));
		assertThat(req.getAttribute("key 1"), is((Object) "value 1"));
		assertThat(req.getAttribute("key 2"), is((Object) "value 2"));
		assertThat(req.getAttribute("key 3"), is((Object) "value 3"));
	}

	@Test
	public void shouldAllowModelAttributesToOverrideGlobalModelAttributes() {
		resolver.addToGlobalModel("key 1", "value 1");
		resolver.resolve(req, resp, new JspView("view.jsp", Expressive.<String, Object> map("key 1", "some other value")));
		assertThat(req.getAttribute("key 1"), is((Object) "some other value"));
	}

	@Test
	public void shouldAllowRemovalOfGlobalModelAttributes() {
		resolver.addToGlobalModel("key 1", "value 1");
		resolver.removeFromGlobalModel("key 1");
		resolver.resolve(req, resp, new JspView("view.jsp", Expressive.<String, Object> map()));
		assertThat(req.getAttribute("key 1"), is(nullValue()));
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenServletContextCannotGetJspResource() throws MalformedURLException {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to resolve JSP view view.jsp (/WEB-INF/jsp/view.jsp) - resource /WEB-INF/jsp/view.jsp does not exist");
		when(servletContext.getResource(anyString())).thenReturn(null);
		resolver.resolve(req, resp, new JspView("view.jsp", Expressive.<String, Object> map()));
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenDispatcherIncludeThrowsServletException() throws ServletException, IOException {
		RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
		doThrow(new ServletException("Internal server error")).when(requestDispatcher).include(req, resp);
		req.requestDispatcher(requestDispatcher);

		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to resolve JSP view view.jsp (/WEB-INF/jsp/view.jsp) - Internal server error");

		resolver.resolve(req, resp, new JspView("view.jsp", Expressive.<String, Object> map()));
	}
}
