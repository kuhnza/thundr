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
package com.threewks.thundr.view.file;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.http.HttpSupport;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;
import com.threewks.thundr.view.ViewResolutionException;

public class FileViewResolverTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MockHttpServletResponse resp = new MockHttpServletResponse();
	private byte[] data = new byte[] { 1, 2, 3 };
	private FileView fileView = new FileView("filename.ext", data, "content/type");
	private FileViewResolver fileViewResolver = new FileViewResolver();

	@Test
	public void shouldWriteContentTypeAndFilenameToHeaders() {
		fileViewResolver.resolve(req, resp, fileView);

		assertThat(resp.getContentType(), is("content/type"));
		assertThat(resp.containsHeader(HttpSupport.Header.ContentDisposition), is(true));
		assertThat((String) resp.header(HttpSupport.Header.ContentDisposition), is("attachment; filename=filename.ext"));
	}

	@Test
	public void shouldThrowViewResolutionExceptionWhenFailedToWriteToOutputStream() {
		thrown.expect(ViewResolutionException.class);
		thrown.expectMessage("Failed to write FileView result: Expected exception");

		fileView = spy(fileView);
		when(fileView.getContentType()).thenThrow(new RuntimeException("Expected exception"));

		fileViewResolver.resolve(req, resp, fileView);
	}
}
