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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.threewks.thundr.util.Streams;

public class FileViewTest {

	@Test
	public void shouldRetainGivenInputStreamContentTypeAndFilename() {
		InputStream stream = mock(InputStream.class);
		FileView fileView = new FileView("filename.ext", stream, "contentType");
		assertThat(fileView.getData(), is(sameInstance(stream)));
		assertThat(fileView.getFileName(), is("filename.ext"));
		assertThat(fileView.getContentType(), is("contentType"));
	}

	@Test
	public void shouldCreateInputStreamForFileAndRetainContentTypeAndFilename() throws FileNotFoundException, URISyntaxException {
		URI resource = this.getClass().getClassLoader().getResource("streams.txt").toURI();
		File file = new File(resource);

		FileView fileView = new FileView("filename.ext", file, "contentType");
		InputStream data = fileView.getData();
		// 'Test data' is the content in the file
		assertThat(Streams.readString(data, "UTF-8"), is("Test data"));
		assertThat(fileView.getFileName(), is("filename.ext"));
		assertThat(fileView.getContentType(), is("contentType"));
	}

	@Test
	public void shouldCreateInputStreamForByteArrayAndRetainContentTypeAndFilename() throws UnsupportedEncodingException {
		byte[] data = "Test data".getBytes("UTF-8");
		FileView fileView = new FileView("filename.ext", data, "contentType");

		InputStream is = fileView.getData();
		assertThat(Streams.readString(is, "UTF-8"), is("Test data"));
		assertThat(fileView.getFileName(), is("filename.ext"));
		assertThat(fileView.getContentType(), is("contentType"));
	}

	@Test
	public void shouldHaveToStringReturningFilenameAndContentType() throws UnsupportedEncodingException {
		byte[] data = "Test data".getBytes("UTF-8");

		FileView fileView = new FileView("filename.ext", data, "content/type");
		assertThat(fileView.toString(), is("filename.ext (content/type)"));
	}
}
