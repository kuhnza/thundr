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
package com.threewks.thundr.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import jodd.io.FastByteArrayOutputStream;

import com.threewks.thundr.exception.BaseException;

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
