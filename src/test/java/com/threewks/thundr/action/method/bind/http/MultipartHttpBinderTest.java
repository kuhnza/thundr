package com.threewks.thundr.action.method.bind.http;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.TestSupport;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class MultipartHttpBinderTest {
	private HttpBinder httpBinder = new HttpBinder();
	private MultipartHttpBinder binder = new MultipartHttpBinder(httpBinder);
	private MockHttpServletRequest request = new MockHttpServletRequest().contentType(ContentType.MultipartFormData);
	private MockHttpServletResponse response = new MockHttpServletResponse();
	private Map<String, String> pathVariables;
	private Map<ParameterDescription, Object> parameterDescriptions;
	private ArrayList<FileItemStream> multipartData;

	@Before
	public void before() throws FileUploadException, IOException {
		parameterDescriptions = new LinkedHashMap<ParameterDescription, Object>();
		pathVariables = new HashMap<String, String>();

		multipartData = new ArrayList<FileItemStream>();
		ServletFileUpload mockUpload = mock(ServletFileUpload.class);
		when(mockUpload.getItemIterator(request)).thenAnswer(new Answer<FileItemIterator>() {

			@Override
			public FileItemIterator answer(InvocationOnMock invocation) throws Throwable {
				return new FileItemIterator() {
					Iterator<FileItemStream> iterator = multipartData.iterator();

					@Override
					public FileItemStream next() throws FileUploadException, IOException {
						return iterator.next();
					}

					@Override
					public boolean hasNext() throws FileUploadException, IOException {
						return iterator.hasNext();
					}
				};
			}
		});
		TestSupport.setField(binder, "upload", mockUpload);
	}

	@Test
	public void shouldOnlyBindMultipartContent() {
		request.contentType(ContentType.ApplicationFormUrlEncoded);
		ParameterDescription field1 = new ParameterDescription("field1", String.class);
		ParameterDescription field2 = new ParameterDescription("field2", String.class);
		addFormField("field1", "value1");
		addFormField("field2", "value2");
		addFormField("field3", "value3");
		parameterDescriptions.put(field1, null);
		parameterDescriptions.put(field2, null);

		binder.bindAll(parameterDescriptions, request, response, pathVariables);
		assertThat(parameterDescriptions.get(field1), is(nullValue()));
		assertThat(parameterDescriptions.get(field2), is(nullValue()));

		request.contentType(ContentType.MultipartFormData);
		binder.bindAll(parameterDescriptions, request, response, pathVariables);
		assertThat(parameterDescriptions.get(field1), is(notNullValue()));
		assertThat(parameterDescriptions.get(field2), is(notNullValue()));
	}

	@Test
	public void shouldBindFormFieldsByDelegatingToHttpBinder() {
		ParameterDescription field1 = new ParameterDescription("field1", String.class);
		ParameterDescription field2 = new ParameterDescription("field2", String.class);
		addFormField("field1", "value1");
		addFormField("field2", "value2");
		addFormField("field3", "value3");
		parameterDescriptions.put(field1, null);
		parameterDescriptions.put(field2, null);

		binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(parameterDescriptions.get(field1), is((Object) "value1"));
		assertThat(parameterDescriptions.get(field2), is((Object) "value2"));
		assertThat(parameterDescriptions.size(), is(2));
	}

	@Test
	public void shouldBindByteArrayFromFileData() {
		ParameterDescription field1 = new ParameterDescription("field1", String.class);
		ParameterDescription field2 = new ParameterDescription("field2", String.class);
		ParameterDescription data = new ParameterDescription("data", byte[].class);
		addFormField("field1", "value1");
		addFormField("field2", "value2");
		addFileField("data", new byte[] { 1, 2, 3 });
		parameterDescriptions.put(field1, null);
		parameterDescriptions.put(field2, null);
		parameterDescriptions.put(data, null);

		binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(parameterDescriptions.get(field1), is((Object) "value1"));
		assertThat(parameterDescriptions.get(field2), is((Object) "value2"));
		assertThat(parameterDescriptions.get(data), is((Object) new byte[] { 1, 2, 3 }));
		assertThat(parameterDescriptions.size(), is(3));
	}

	private void addFormField(final String name, final String value) {
		multipartData.add(new FileItemStream() {
			@Override
			public void setHeaders(FileItemHeaders headers) {
			}

			@Override
			public FileItemHeaders getHeaders() {
				return null;
			}

			@Override
			public InputStream openStream() throws IOException {
				return new ByteArrayInputStream(value.getBytes("UTF-8"));
			}

			@Override
			public boolean isFormField() {
				return true;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getFieldName() {
				return name;
			}

			@Override
			public String getContentType() {
				return null;
			}
		});
	}

	private void addFileField(final String name, final byte[] data) {
		multipartData.add(new FileItemStream() {
			@Override
			public void setHeaders(FileItemHeaders headers) {
			}

			@Override
			public FileItemHeaders getHeaders() {
				return null;
			}

			@Override
			public InputStream openStream() throws IOException {
				return new ByteArrayInputStream(data);
			}

			@Override
			public boolean isFormField() {
				return false;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getFieldName() {
				return name;
			}

			@Override
			public String getContentType() {
				return null;
			}
		});
	}
}
