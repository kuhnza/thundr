package com.threewks.thundr.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import jodd.servlet.URLCoder;

public class MailHttpServletResponse implements HttpServletResponse {
	private String contentType = "text/html;charset=UTF-8";
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private ServletOutputStream os = new ServletOutputStream() {
		@Override
		public void write(int b) throws IOException {
			baos.write(b);
		}
	};
	private PrintWriter writer = new PrintWriter(baos);

	public String getResponseContent() {
		try {
			writer.flush();
			writer.close();
			os.flush();
			return baos.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to get email content, this platform does not support UTF-8 encoding!: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("Failed to get email content, could not flush a ByteArrayOutputStream!: " + e.getMessage(), e);
		}
	}

	@Override
	public String getCharacterEncoding() {
		return "UTF-8";
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
		return writer;
	}

	@Override
	public void setCharacterEncoding(String charset) {
		// noop
	}

	@Override
	public void setContentLength(int len) {
		// noop
	}

	@Override
	public void setContentType(String type) {
		this.contentType = type;
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
		return URLCoder.url(url);
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
		throw new MailException("Mail send failed(%d): %s", sc, msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		throw new MailException("Mail send failed(%d)", sc);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		throw new MailException("Mail send failed - attempting to redirect to %s", location);
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
