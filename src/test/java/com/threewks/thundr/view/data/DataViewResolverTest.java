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
package com.threewks.thundr.view.data;

import com.threewks.thundr.http.exception.HttpStatusException;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolver;
import com.threewks.thundr.view.ViewResolverRegistry;
import com.threewks.thundr.view.json.JsonView;
import com.threewks.thundr.view.json.JsonViewResolver;
import com.threewks.thundr.view.xml.XmlView;
import com.threewks.thundr.view.xml.XmlViewResolver;
import jodd.util.MimeTypes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import org.junit.rules.ExpectedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlRootElement;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DataViewResolverTest {

	@XmlRootElement(name = "sample")
	private static class Sample {
		public int x = 2;
		public int y = 10;
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private DataViewResolver viewResolver;

	@Before
	public void setUp() {
		ViewResolverRegistry registry = new ViewResolverRegistry();
		ViewResolver jsonResolver = new JsonViewResolver();
		ViewResolver xmlResolver = new XmlViewResolver();
		registry.addResolver(JsonView.class, jsonResolver);
		registry.addResolver(XmlView.class, xmlResolver);

		viewResolver = new DataViewResolver(registry);
		viewResolver.addDataViewType(MimeTypes.MIME_APPLICATION_JSON, JsonView.class);
		viewResolver.addDataViewType(MimeTypes.MIME_APPLICATION_XML, XmlView.class);
	}

	@Test
	public void shouldDetermineCorrectViewResolverUsingFormatParameter() {
		HttpServletRequest req = new MockHttpServletRequest().parameter("format", "xml");
		assertThat(req.getParameter("format"), is("xml"));

		HttpServletResponse resp = new MockHttpServletResponse();
		DataView view = new DataView(new Sample());

		viewResolver.resolve(req, resp, view);
		assertThat(resp.getContentType(), is(MimeTypes.MIME_APPLICATION_XML));
	}

	@Test
	public void shouldDetermineCorrectViewResolverUsingAcceptHeader() {
		HttpServletRequest req = new MockHttpServletRequest().header("Accept", MimeTypes.MIME_APPLICATION_XML);
		assertThat(req.getHeader("Accept"), is(MimeTypes.MIME_APPLICATION_XML));

		HttpServletResponse resp = new MockHttpServletResponse();
		DataView view = new DataView(new Sample());

		viewResolver.resolve(req, resp, view);
		assertThat(resp.getContentType(), is(MimeTypes.MIME_APPLICATION_XML));
	}

	@Test
	public void shouldDetermineCorrectViewResolverUsingMultiAcceptHeader() {
		HttpServletRequest req = new MockHttpServletRequest().header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		HttpServletResponse resp = new MockHttpServletResponse();
		DataView view = new DataView(new Sample());

		viewResolver.resolve(req, resp, view);
		assertThat(resp.getContentType(), is(MimeTypes.MIME_APPLICATION_XML));
	}

	@Test
	public void shouldDefaultToJsonViewResolverWhereNoAcceptHeaderOrFormatParameterPresent() {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse resp = new MockHttpServletResponse();
		DataView view = new DataView(new Sample());

		viewResolver.resolve(req, resp, view);
		assertThat(resp.getContentType(), is(MimeTypes.MIME_APPLICATION_JSON));
	}

	@Test
	public void shouldThrowHttpStatusExceptionWhenPassedUnknownFormat() {
		thrown.expect(HttpStatusException.class);
		thrown.expectMessage("Not acceptable: yaml");

		HttpServletRequest req = new MockHttpServletRequest().parameter("format", "yaml");
		HttpServletResponse resp = new MockHttpServletResponse();
		DataView view = new DataView(new Sample());

		viewResolver.resolve(req, resp, view);
	}
}
