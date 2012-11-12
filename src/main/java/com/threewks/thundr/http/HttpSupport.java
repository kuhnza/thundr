package com.threewks.thundr.http;

import java.net.URI;

public class HttpSupport {
	public static class Header {
		public static final String ContentLength = "Content-Length";
		public static final String ContentType = "Content-Type";
		public static final String ContentDisposition = "Content-Disposition";
		public static final String LastModified = "Last-Modified";
		public static final String Expires = "Expires";
		public static final String CacheControl = "Cache-Control";
		public static final String AcceptEncoding = "Accept-Encoding";
		public static final String ContentEncoding = "Content-Encoding";
		public static final String SetCookie = "Set-Cookie";
		public static final String SetCookie2 = "Set-Cookie2";
		public static final String Pragma = "Pragma";
	}

	public static String convertToValidUrl(String url) {
		try {
			return new URI(url).toASCIIString();
		} catch (Exception e) {
			return url;
		}
	}
}
