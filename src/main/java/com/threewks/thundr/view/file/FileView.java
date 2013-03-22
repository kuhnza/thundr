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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This view is for serving files to controller consumers.
 * The given data (InputStream, byte[] or File) will be streamed to the client with the specified filename (as content-disposition) and content-type.
 * 
 * @see FileViewResolver
 */
public class FileView {
	private InputStream is;
	private String contentType;
	private String fileName;

	public FileView(String filename, InputStream stream, String contentType) {
		this.fileName = filename;
		this.is = stream;
		this.contentType = contentType;
	}

	public FileView(String filename, byte[] data, String contentType) {
		this(filename, new ByteArrayInputStream(data), contentType);
	}

	public FileView(String filename, File file, String contentType) throws FileNotFoundException {
		this(filename, new FileInputStream(file), contentType);
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getData() {
		return is;
	}

	public String getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", fileName, contentType);
	}

}
