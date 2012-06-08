package com.threewks.thundr.action.method.bind.http;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.threewks.thundr.action.method.bind.path.PathVariableBinder;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.TestSupport;

public class MultipartHttpBinderTest {

	private PathVariableBinder pathVariableBinder = new PathVariableBinder();
	private HttpBinder httpBinder = new HttpBinder(pathVariableBinder);
	private MultipartHttpBinder binder = new MultipartHttpBinder(httpBinder);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private Map<String, String> pathVariables;
	private List<ParameterDescription> parameterDescriptions;
	private ArrayList<FileItemStream> multipartData;

	@Before
	public void before() throws FileUploadException, IOException {
		session = mock(HttpSession.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		parameterDescriptions = new ArrayList<ParameterDescription>();
		pathVariables = new HashMap<String, String>();

		when(request.getParameterMap()).thenReturn(Collections.<String, String[]> emptyMap());

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
	public void shouldReturnTrueForMultipartFile() {
		assertThat(binder.canBind(ContentType.MultipartFormData.value()), is(true));
		assertThat(binder.canBind(ContentType.MultipartFormData.value().toUpperCase()), is(true));
	}

	@Test
	public void shouldBindSpecialTypesByDelegatingToHttpBinder() {
		parameterDescriptions.add(new ParameterDescription("request", HttpServletRequest.class));
		parameterDescriptions.add(new ParameterDescription("response", HttpServletResponse.class));
		parameterDescriptions.add(new ParameterDescription("session", HttpSession.class));
		when(request.getSession()).thenReturn(session);

		List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(boundVariables, Matchers.<Object> hasItem(request));
		assertThat(boundVariables, Matchers.<Object> hasItem(response));
		assertThat(boundVariables, Matchers.<Object> hasItem(session));
		assertThat(boundVariables.size(), is(3));
	}

	@Test
	public void shouldBindFormFieldsByDelegatingToHttpBinder() {
		addFormField("field1", "value1");
		addFormField("field2", "value2");
		addFormField("field3", "value3");
		parameterDescriptions.add(new ParameterDescription("field1", String.class));
		parameterDescriptions.add(new ParameterDescription("field2", String.class));

		List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(boundVariables, Matchers.<Object> hasItem("value1"));
		assertThat(boundVariables, Matchers.<Object> hasItem("value2"));
		assertThat(boundVariables.size(), is(2));
	}

	@Test
	public void shouldBindByteArrayFromFileData() {
		addFormField("field1", "value1");
		addFormField("field2", "value2");
		addFileField("data", new byte[] { 1, 2, 3 });
		parameterDescriptions.add(new ParameterDescription("field1", String.class));
		parameterDescriptions.add(new ParameterDescription("field2", String.class));
		parameterDescriptions.add(new ParameterDescription("data", byte[].class));

		List<Object> boundVariables = binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(boundVariables, Matchers.<Object> hasItem("value1"));
		assertThat(boundVariables, Matchers.<Object> hasItem("value2"));
		assertThat(boundVariables, Matchers.<Object> hasItem(new byte[] { 1, 2, 3 }));
		assertThat(boundVariables.size(), is(3));
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
