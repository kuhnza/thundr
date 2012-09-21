package com.threewks.thundr.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some common ContentType definitions for MIME types.
 * For a world of fun, check out http://www.iana.org/assignments/media-types/index.html
 */
public enum ContentType {
	TextPlain("text/plain"),
	TextHtml("text/html"), 
	TextCss("text/css"),
	TextCsv("text/csv"),
	TextJavascript("text/javascript"),
	ApplicationFormUrlEncoded("application/x-www-form-urlencoded"),
	ApplicationOctetStream("application/octet-stream"),
	ApplicationJson("application/json"),
	ApplicationXml("application/xml"),
	ApplicationXmlDtd("application/xml-dtd"),
	ApplicationSoapXml("application/soap+xml"),
	MultipartFormData("multipart/form-data"),
	ImageJpeg("image/jpeg"),
	ImageGif("image/gif"),
	ImagePng("image/png");

	private String value;

	private ContentType(String encoding) {
		this.value = encoding;
	}

	public String value() {
		return value;
	}

	/**
	 * Returns true if the given string content type matches the content type represented by this enum value.
	 * 
	 * @param contentType
	 * @return
	 */
	public boolean matches(String contentType) {
		return this.value.equalsIgnoreCase(cleanContentType(contentType));
	}

	private static final Pattern contentTypePattern = Pattern.compile("([\\w+-]+)/([\\w+-]+?)(\\s*;.*)?");

	/**
	 * Removes extraneous information from the content-type header, such as encoding etc.
	 * If this is not recognised as a content type, returns the input.
	 * 
	 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
	 * @param rawContentType
	 * @return
	 */
	public static String cleanContentType(String rawContentType) {
		if (rawContentType != null) {
			Matcher matcher = contentTypePattern.matcher(rawContentType);
			if (matcher.matches()) {
				return (matcher.group(1) + "/" + matcher.group(2)).toLowerCase();
			}
		}
		return rawContentType;
	}

	public static ContentType from(String encoding) {
		encoding = cleanContentType(encoding);
		for (ContentType contentType : values()) {
			if (contentType.value.equalsIgnoreCase(encoding)) {
				return contentType;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}
}
