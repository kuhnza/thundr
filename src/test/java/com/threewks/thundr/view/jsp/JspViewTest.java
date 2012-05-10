package com.threewks.thundr.view.jsp;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

public class JspViewTest {
	@Test
	public void shouldSaveViewPathAndHaveEmptyModel() {
		JspView view = new JspView("/WEB-INF/jsp/path/view.jsp");
		assertThat(view.getView(), is("/WEB-INF/jsp/path/view.jsp"));
		assertThat(view.getModel(), is(notNullValue()));
		assertThat(view.getModel().isEmpty(), is(true));
	}

	@Test
	public void shouldSaveViewPathAndModel() {
		Map<String, Object> model = map("input", 1);
		JspView view = new JspView("/WEB-INF/jsp/path/view.jsp", model);
		assertThat(view.getView(), is("/WEB-INF/jsp/path/view.jsp"));
		assertThat(view.getModel(), is(notNullValue()));
		assertThat(view.getModel().get("input"), is((Object) 1));
	}

	@Test
	public void shouldReturnViewPathRelativeToWebInfAndForJspWhenPartialViewNameGiven() {
		assertThat(new JspView("view").getView(), is("/WEB-INF/jsp/view.jsp"));
		assertThat(new JspView("view.jsp").getView(), is("/WEB-INF/jsp/view.jsp"));
		assertThat(new JspView("path/view.jsp").getView(), is("/WEB-INF/jsp/path/view.jsp"));
		assertThat(new JspView("path/view").getView(), is("/WEB-INF/jsp/path/view.jsp"));
	}

	@Test
	public void shouldReturnViewNameForToString() {
		assertThat(new JspView("/WEB-INF/jsp/view.jsp").toString(), is("/WEB-INF/jsp/view.jsp"));
		assertThat(new JspView("view").toString(), is("view (/WEB-INF/jsp/view.jsp)"));
		assertThat(new JspView("view.jsp").toString(), is("view.jsp (/WEB-INF/jsp/view.jsp)"));
		assertThat(new JspView("path/view.jsp").toString(), is("path/view.jsp (/WEB-INF/jsp/path/view.jsp)"));
		assertThat(new JspView("path/view").toString(), is("path/view (/WEB-INF/jsp/path/view.jsp)"));
	}
}
