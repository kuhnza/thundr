package com.atomicleopard.webFramework.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import jodd.io.FastByteArrayOutputStream;

import com.atomicleopard.webFramework.exception.BaseException;

public class Streams {
	private static final String DefaultEncoding = "UTF-8";

	public static String readString(InputStream inputStream) {
		return readString(inputStream, DefaultEncoding);
	}

	public static String readString(InputStream inputStream, String encoding) {
		try {
			byte[] data = readBytes(inputStream);
			return new String(data, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e, "Could not create string with encoding %s: %s", encoding, e.getMessage());
		}
	}

	public static byte[] readBytes(InputStream inputStream) {
		try {
			FastByteArrayOutputStream byteArrayOutputStream = new FastByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int read;
			while ((read = inputStream.read(buffer)) > -1) {
				byteArrayOutputStream.write(buffer, 0, read);
			}
			byteArrayOutputStream.flush();
			byteArrayOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new BaseException(e, "Failed to read from InputStream: %s", e.getMessage());
		}
	}

	public static byte[] getResourceAsBytes(String resource) {
		try {
			InputStream resourceAsStream = getResourceAsStream(resource);
			return readBytes(resourceAsStream);
		} catch (Exception e) {
			throw new BaseException(e, "Could not load resource %s: %s", resource, e.getMessage());
		}
	}

	public static String getResourceAsString(String resource) {
		try {
			InputStream resourceAsStream = getResourceAsStream(resource);
			return readString(resourceAsStream);
		} catch (Exception e) {
			throw new BaseException(e, "Could not load resource %s: %s", resource, e.getMessage());
		}
	}

	public static InputStream getResourceAsStream(String resource) {
		try {
			InputStream resourceAsStream = Streams.class.getClassLoader().getResourceAsStream(resource);
			return new BufferedInputStream(resourceAsStream);
		} catch (Exception e) {
			throw new BaseException(e, "Could not load resource %s: %s", resource, e.getMessage());
		}
	}
}
