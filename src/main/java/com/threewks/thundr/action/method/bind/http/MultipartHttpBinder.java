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
package com.threewks.thundr.action.method.bind.http;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.action.method.bind.ActionMethodBinder;
import com.threewks.thundr.action.method.bind.BindException;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.util.Streams;

public class MultipartHttpBinder implements ActionMethodBinder {
	private static final String[] emptyStringArray = new String[0];
	private List<ContentType> supportedContentTypes = Arrays.asList(ContentType.MultipartFormData);
	private ServletFileUpload upload = new ServletFileUpload();
	private HttpBinder httpBinder;
	private List<BinaryParameterBinder<?>> binders = binders();

	public MultipartHttpBinder(HttpBinder httpBinder) {
		this.httpBinder = httpBinder;
	}

	@Override
	public void bindAll(Map<ParameterDescription, Object> bindings, HttpServletRequest req, HttpServletResponse resp, Map<String, String> pathVariables) {
		if (ContentType.anyMatch(supportedContentTypes, req.getContentType())) {
			Map<String, List<String>> formFields = new HashMap<String, List<String>>();
			Map<String, MultipartFile> fileFields = new HashMap<String, MultipartFile>();
			extractParameters(req, formFields, fileFields);
			Map<String, String[]> parameterMap = convertListMapToArrayMap(formFields);
			httpBinder.bind(bindings, req, resp, parameterMap);
			bindBinaryParameters(bindings, fileFields);
		}
	}

	private void bindBinaryParameters(Map<ParameterDescription, Object> bindings, Map<String, MultipartFile> fileFields) {
		for (ParameterDescription parameterDescription : bindings.keySet()) {
			if (bindings.get(parameterDescription) == null) {
				Object value = bindParameter(fileFields, parameterDescription);
				bindings.put(parameterDescription, value);
			}
		}
	}

	private Object bindParameter(Map<String, MultipartFile> fileFields, ParameterDescription parameterDescription) {
		Object value = null;
		BinaryParameterBinder<?> binder = findParamterBinder(parameterDescription);
		if (binder != null) {
			String name = parameterDescription.name();
            MultipartFile file= fileFields.get(name);
			value = binder.bind(parameterDescription, file);
		}
		return value;
	}

	private BinaryParameterBinder<?> findParamterBinder(ParameterDescription parameterDescription) {
		BinaryParameterBinder<?> binder = null;
		for (BinaryParameterBinder<?> parameterBinder : binders) {
			if (parameterBinder.willBind(parameterDescription)) {
				binder = parameterBinder;
			}
		}
		return binder;
	}

	private void extractParameters(HttpServletRequest req, Map<String, List<String>> formFields, Map<String, MultipartFile> fileFields) {
		try {
			FileItemIterator itemIterator = upload.getItemIterator(req);
			while (itemIterator.hasNext()) {
				FileItemStream item = itemIterator.next();
				InputStream stream = item.openStream();

                String fieldName = item.getFieldName();
				if (item.isFormField()) {
					List<String> existing = formFields.get(fieldName);
					if (existing == null) {
						existing = new LinkedList<String>();
						formFields.put(fieldName, existing);
					}
					existing.add(Streams.readString(stream));
				} else {
					fileFields.put(fieldName, new MultipartFile(item.getName(), Streams.readBytes(stream), item.getContentType()));
				}
				stream.close();
			}
		} catch (Exception e) {
			throw new BindException(e, "Failed to bind multipart form data: %s", e.getMessage());
		}
	}

	private Map<String, String[]> convertListMapToArrayMap(Map<String, List<String>> formFields) {
		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		for (Map.Entry<String, List<String>> formFieldEntry : formFields.entrySet()) {
			parameterMap.put(formFieldEntry.getKey(), formFieldEntry.getValue().toArray(emptyStringArray));
		}
		return parameterMap;
	}

	private static List<BinaryParameterBinder<?>> binders() {
		return Expressive.<BinaryParameterBinder<?>> list(new ByteArrayBinaryParameterBinder(), new InputStreamBinaryParameterBinder(), new MultipartFileParameterBinder());
	}
}
