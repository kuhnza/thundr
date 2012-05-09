package com.atomicleopard.webFramework.view.jsp;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletRequest;
import com.atomicleopard.webFramework.test.mock.servlet.MockHttpServletResponse;

public class SpecificJspViewResolverTest {
	private MockHttpServletRequest mockRequest = new MockHttpServletRequest();
	private MockHttpServletResponse mockResponse = new MockHttpServletResponse();

	@Test
	public void shouldRetainGivenJsp() {
		assertThat(new SpecificJspViewResolver<Object>("/WEB-INF/jsp/something/page.jsp").jspPath(), is("/WEB-INF/jsp/something/page.jsp"));
	}

	@Test
	public void shouldDefaultGivenValueToWebInfJspPath() {
		assertThat(new SpecificJspViewResolver<Object>("something/page.jsp").jspPath(), is("/WEB-INF/jsp/something/page.jsp"));
	}

	@Test
	public void shouldAddViewResultAsRequestAttributeWithNameValue() {
		SpecificJspViewResolver<Object> resolver = new SpecificJspViewResolver<Object>("page.jsp");
		Object viewResult = new Object();
		resolver.resolve(mockRequest, mockResponse, viewResult);
		assertThat(mockRequest.getAttribute("value"), is(viewResult));
	}

	@Test
	public void shouldIncludeTheSpecifiedJsp() {
		SpecificJspViewResolver<Object> resolver = new SpecificJspViewResolver<Object>("page.jsp");
		Object viewResult = new Object();
		resolver.resolve(mockRequest, mockResponse, viewResult);
		assertThat(mockRequest.getAttribute("value"), is(viewResult));
		assertThat(mockRequest.requestDispatcher().lastPath(), is("/WEB-INF/jsp/page.jsp"));
		assertThat(mockRequest.requestDispatcher().included(), is(true));
	}
}
