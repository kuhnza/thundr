package com.atomicleopard.webFramework.http;

import java.net.URI;

public class HttpSupport {
	public static final String HttpHeaderContentLength = "Content-Length";
	public static final String HttpHeaderContentType = "Content-Type";
	public static final String HttpHeaderLastModified = "Last-Modified";
	public static final String HttpHeaderExpires = "Expires";
	public static final String HttpHeaderCacheControl = "Cache-Control";
	public static final String HttpHeaderAcceptEncoding = "Accept-Encoding";

	public static String convertToValidUrl(String url) {
		try {
			return new URI(url).toASCIIString();
		} catch (Exception e) {
			return url;
		}
	}
}
