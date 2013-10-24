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
package com.threewks.thundr.http;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.threewks.thundr.exception.BaseException;

public class SyntheticHttpServletResponseTest {
	private SyntheticHttpServletResponse syntheticHttpServletResponse = new SyntheticHttpServletResponse();

	@Rule public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldCaptureBasicServletOutputStreamContent() throws IOException {
		ServletOutputStream outputStream = syntheticHttpServletResponse.getOutputStream();
		outputStream.write("This is my test string".getBytes("UTF-8"));

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("This is my test string"));
	}

	@Test
	public void shouldCaptureBasicPrintWriterContent() throws IOException {
		PrintWriter writer = syntheticHttpServletResponse.getWriter();
		writer.print("This is my test string");

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("This is my test string"));
	}

	@Test
	public void shouldCaptureServletOutputStreamContentWithSpecifiedCharacterEncoding() throws IOException {
		syntheticHttpServletResponse.setCharacterEncoding("ISO-8859-1");

		ServletOutputStream outputStream = syntheticHttpServletResponse.getOutputStream();
		outputStream.write("This is my test string".getBytes("ISO-8859-1"));

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("This is my test string"));
	}

	@Test
	public void shouldCapturePrintWriterContentWithSpecifiedCharacterEncoding() throws IOException {
		syntheticHttpServletResponse.setCharacterEncoding("ISO-8859-1");

		PrintWriter writer = syntheticHttpServletResponse.getWriter();
		writer.print("This is my test string");

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("This is my test string"));
	}

	@Test
	public void shouldSetCharacterEncoding() {
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("UTF-8"));

		syntheticHttpServletResponse.setCharacterEncoding("ISO-8859-1");
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("ISO-8859-1"));
		
		syntheticHttpServletResponse.setCharacterEncoding("");
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is(nullValue()));
		
		syntheticHttpServletResponse.setCharacterEncoding("utf-16");
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("UTF-16"));
		
		syntheticHttpServletResponse.setCharacterEncoding(null);
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is(nullValue()));
	}

	@Test
	public void shouldSetContentType() {
		assertThat(syntheticHttpServletResponse.getContentType(), is("text/html"));

		syntheticHttpServletResponse.setContentType("text/plain");
		assertThat(syntheticHttpServletResponse.getContentType(), is("text/plain"));

		syntheticHttpServletResponse.setContentType("   ");
		assertThat(syntheticHttpServletResponse.getContentType(), is(""));

		syntheticHttpServletResponse.setContentType(null);
		assertThat(syntheticHttpServletResponse.getContentType(), is(nullValue()));
		
		syntheticHttpServletResponse.setContentType("something ; whatever");
		assertThat(syntheticHttpServletResponse.getContentType(), is("something"));
	}

	@Test
	public void shouldSetCharacterEncodingWhenSettingContentTypeIfSpecified() {
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("UTF-8"));
		assertThat(syntheticHttpServletResponse.getContentType(), is("text/html"));

		syntheticHttpServletResponse.setContentType("text/plain; charset=ISO-8859-1");

		assertThat(syntheticHttpServletResponse.getContentType(), is("text/plain"));
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("ISO-8859-1"));
	}

	@Test
	public void shouldSetCharacterEncodingWhenSettingContentTypeInAnyCaseIfSpecified() {
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("UTF-8"));
		assertThat(syntheticHttpServletResponse.getContentType(), is("text/html"));

		syntheticHttpServletResponse.setContentType("TEXT/PLAIN; CHARSET=iso-8859-1");

		assertThat(syntheticHttpServletResponse.getContentType(), is("text/plain"));
		assertThat(syntheticHttpServletResponse.getCharacterEncoding(), is("ISO-8859-1"));
	}

	@Test
	public void shouldAllowWritingUsingDifferentServletOutputStreamMethods() throws IOException {
		ServletOutputStream outputStream = syntheticHttpServletResponse.getOutputStream();
		outputStream.write("This is my test string".getBytes("UTF-8"));
		outputStream.write((byte) 32);
		outputStream.write("and other".getBytes("UTF-8"), 0, 9);
		outputStream.print(" ways");
		outputStream.print(' ');
		outputStream.print(123);
		outputStream.print(true);

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("This is my test string and other ways 123true"));
	}

	@Test
	public void shouldThrowExceptionWhenCharacterEncodingIsNotLegalOnThisPlatform() throws IOException {
		thrown.expect(BaseException.class);
		thrown.expectMessage("Failed to get output, this platform does not support the specified character encoding 'UTF-7': UTF-7");

		ServletOutputStream outputStream = syntheticHttpServletResponse.getOutputStream();
		outputStream.write("This is my test string".getBytes("UTF-8"));
		syntheticHttpServletResponse.setCharacterEncoding("UTF-7");
		syntheticHttpServletResponse.getResponseContent();
	}

	@Test
	public void shouldThrowExceptionOnSendError() throws IOException {
		thrown.expect(BaseException.class);
		thrown.expectMessage("Writing to SyntheticHttpServletResponse failed (500 - Internal Server Error)");

		syntheticHttpServletResponse.sendError(500);
	}

	@Test
	public void shouldThrowExceptionOnSendErrorWithMessage() throws IOException {
		thrown.expect(BaseException.class);
		thrown.expectMessage("Writing to SyntheticHttpServletResponse failed (500 - Internal Server Error): failed message");

		syntheticHttpServletResponse.sendError(500, "failed message");
	}

	@Test
	public void shouldThrowExceptionOnRedirect() throws IOException {
		thrown.expect(BaseException.class);
		thrown.expectMessage("Writing to SyntheticHttpServletResponse failed - attempting to redirect to /page/?param");

		syntheticHttpServletResponse.sendRedirect("/page/?param");
	}

	@Test
	public void shouldReturnDefaultLocaleAlways() {
		Locale original = syntheticHttpServletResponse.getLocale();
		assertThat(original, is(notNullValue()));

		syntheticHttpServletResponse.setLocale(new Locale("JP"));
		assertThat(syntheticHttpServletResponse.getLocale(), is(original));
	}

	@Test
	public void shouldEncodeGivenUrl() {
		assertThat(syntheticHttpServletResponse.encodeUrl("http://hi.com/what is?this=something&a"), is("http://hi.com/what%20is?this=something&a"));
		assertThat(syntheticHttpServletResponse.encodeURL("http://hi.com/what is?this=something&a"), is("http://hi.com/what%20is?this=something&a"));
	}

	@Test
	public void shouldEncodeRedirectUrl() {
		assertThat(syntheticHttpServletResponse.encodeRedirectUrl("http://hi.com/what is?this=something&a"), is("http://hi.com/what%20is?this=something&a"));
		assertThat(syntheticHttpServletResponse.encodeRedirectURL("http://hi.com/what is?this=something&a"), is("http://hi.com/what%20is?this=something&a"));
	}

	@Test
	public void shouldNoopOnSettingOfHeaders() {
		syntheticHttpServletResponse.setHeader("name", "value");
		syntheticHttpServletResponse.setDateHeader("date", 123L);
		syntheticHttpServletResponse.setIntHeader("int", 123);
		assertThat(syntheticHttpServletResponse.containsHeader("name"), is(false));
		assertThat(syntheticHttpServletResponse.containsHeader("date"), is(false));
		assertThat(syntheticHttpServletResponse.containsHeader("int"), is(false));

		syntheticHttpServletResponse.addDateHeader("date", 123L);
		syntheticHttpServletResponse.addHeader("name", "value");
		syntheticHttpServletResponse.addIntHeader("int", 123);
		assertThat(syntheticHttpServletResponse.containsHeader("name"), is(false));
		assertThat(syntheticHttpServletResponse.containsHeader("date"), is(false));
		assertThat(syntheticHttpServletResponse.containsHeader("int"), is(false));

		assertThat(syntheticHttpServletResponse.getResponseContent(), is(""));
	}

	@Test
	public void shouldNoopOnSettingStatus() {
		syntheticHttpServletResponse.setStatus(123);
		syntheticHttpServletResponse.setStatus(456, "message");
		assertThat(syntheticHttpServletResponse.getResponseContent(), is(""));
	}

	@Test
	public void shouldNoopOnAddCookie() {
		syntheticHttpServletResponse.addCookie(Cookies.build("cookie").withValue("value").build());

		assertThat(syntheticHttpServletResponse.containsHeader(HttpSupport.Header.SetCookie), is(false));
		assertThat(syntheticHttpServletResponse.getResponseContent(), is(""));
	}

	@Test
	public void shouldNoopOnResetAndFlushes() throws IOException {
		syntheticHttpServletResponse.setContentType("text/plain;UTF-16");
		syntheticHttpServletResponse.getWriter().print("Output content");

		syntheticHttpServletResponse.reset();
		syntheticHttpServletResponse.resetBuffer();
		syntheticHttpServletResponse.flushBuffer();
		syntheticHttpServletResponse.setContentLength(1);

		assertThat(syntheticHttpServletResponse.getResponseContent(), is("Output content"));
	}

	@Test
	public void shouldHaveBufferSizeAsNoop() {
		assertThat(syntheticHttpServletResponse.getBufferSize(), is(0));
		syntheticHttpServletResponse.setBufferSize(123);
		assertThat(syntheticHttpServletResponse.getBufferSize(), is(0));
	}

	@Test
	public void shouldAlwaysReturnFalseForIsCommitted() throws IOException {
		assertThat(syntheticHttpServletResponse.isCommitted(), is(false));
		syntheticHttpServletResponse.getWriter().flush();
		syntheticHttpServletResponse.getWriter().close();
		assertThat(syntheticHttpServletResponse.isCommitted(), is(false));
	}
}
