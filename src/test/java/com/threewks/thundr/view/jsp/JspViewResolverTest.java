package com.threewks.thundr.view.jsp;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

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
}
