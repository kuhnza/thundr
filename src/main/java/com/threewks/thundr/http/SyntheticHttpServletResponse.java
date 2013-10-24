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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringPool;
import jodd.util.URLCoder;

import org.apache.commons.lang3.StringUtils;

import com.threewks.thundr.exception.BaseException;

public class SyntheticHttpServletResponse implements HttpServletResponse {
	private String contentType = ContentType.TextHtml.value();
	private String characterEncoding = StringPool.UTF_8;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private ServletOutputStream os = new ServletOutputStream() {
		@Override
		public void write(int b) throws IOException {
			baos.write(b);
		}

		public void write(byte[] arg0) throws IOException {
			baos.write(arg0);
		};

		public void write(byte[] b, int off, int len) throws IOException {
			baos.write(b, off, len);
		};

	};
	private PrintWriter writer;

	/**
	 * Returns the content sent in this synthetic response. The content interprets the underlying bytes written to the response using the specified character encoding.
	 * 
	 * @return
	 */
	public String getResponseContent() {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			os.flush();
			return baos.toString(characterEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e, "Failed to get output, this platform does not support the specified character encoding '%s': %s", characterEncoding, e.getMessage());
		} catch (IOException e) {
			throw new BaseException(e, "Failed to get output, could not flush a ByteArrayOutputStream!: ", e.getMessage());
		}
	}

	@Override
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		this.characterEncoding = StringUtils.trimToNull(StringUtils.upperCase(charset));
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return os;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			writer = new PrintWriter(new OutputStreamWriter(baos, characterEncoding));
		}
		return writer;
	}

	@Override
	public void setContentType(String type) {
		String[] contentTypeAndCharacterEncoding = type == null ? new String[] { null } : type.split(";");
		this.contentType = StringUtils.trim(StringUtils.lowerCase(contentTypeAndCharacterEncoding[0]));
		if (contentTypeAndCharacterEncoding.length > 1) {
			String encoding = StringUtils.trimToEmpty(contentTypeAndCharacterEncoding[1]);
			encoding = encoding.replaceAll("(?i)charset=", "");
			setCharacterEncoding(encoding);
		}
	}

	@Override
	public void setContentLength(int len) {
		// noop
	}

	@Override
	public void setBufferSize(int size) {
		// noop
	}

	@Override
	public int getBufferSize() {
		return 0;
	}

	@Override
	public void flushBuffer() throws IOException {
		// noop
	}

	@Override
	public void resetBuffer() {
		// noop
	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public void reset() {
		// noop
	}

	@Override
	public void setLocale(Locale loc) {
		// noop
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public void addCookie(Cookie cookie) {
		// noop
	}

	@Override
	public boolean containsHeader(String name) {
		return false;
	}

	@Override
	public String encodeURL(String url) {
		return URLCoder.encodeUrl(url);
	}

	@Override
	public String encodeRedirectURL(String url) {
		return encodeURL(url);
	}

	@Override
	public String encodeUrl(String url) {
		return encodeURL(url);
	}

	@Override
	public String encodeRedirectUrl(String url) {
		return encodeURL(url);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		throw new BaseException("Writing to %s failed (%d - %s): %s", this.getClass().getSimpleName(), sc, HttpSupport.getReasonForHttpStatus(sc), msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		throw new BaseException("Writing to %s failed (%d - %s)", this.getClass().getSimpleName(), sc, HttpSupport.getReasonForHttpStatus(sc));
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		throw new BaseException("Writing to %s failed - attempting to redirect to %s", this.getClass().getSimpleName(), location);
	}

	@Override
	public void setDateHeader(String name, long date) {
		// noop
	}

	@Override
	public void addDateHeader(String name, long date) {
		// noop
	}

	@Override
	public void setHeader(String name, String value) {
		// noop
	}

	@Override
	public void addHeader(String name, String value) {
		// noop
	}

	@Override
	public void setIntHeader(String name, int value) {
		// noop
	}

	@Override
	public void addIntHeader(String name, int value) {
		// noop
	}

	@Override
	public void setStatus(int sc) {
		// noop
	}

	@Override
	public void setStatus(int sc, String sm) {
		// noop
	}
}
