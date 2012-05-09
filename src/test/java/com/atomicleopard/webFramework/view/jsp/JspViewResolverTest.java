package com.atomicleopard.webFramework.view.jsp;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletRequest;
import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletResponse;

public class JspViewResolverTest {
	private JspViewResolver resolver = new JspViewResolver();
	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();

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
}
