package com.threewks.thundr.action.method.bind.http;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class HttpBinderTest {
	private HttpBinder binder;
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private HttpServletResponse response = new MockHttpServletResponse();
	private Map<String, String> pathVariables;
	private Map<ParameterDescription, Object> parameterDescriptions;

	@Before
	public void before() {
		binder = new HttpBinder();

		parameterDescriptions = new LinkedHashMap<ParameterDescription, Object>();
		pathVariables = new HashMap<String, String>();
	}

	@Test
	public void shouldBindNullContentType() {
		request.contentType(ContentType.Null);
		request.parameter("param1", "1");

		ParameterDescription param1 = new ParameterDescription("param1", int.class);

		request.contentType((String) null);
		parameterDescriptions = map(param1, null);
		binder.bindAll(parameterDescriptions, request, response, pathVariables);
		assertThat(parameterDescriptions.get(param1), is((Object) 1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindValidContentTypes() {
		request.contentType(ContentType.Null);
		request.parameter("param1", "1");

		ParameterDescription param1 = new ParameterDescription("param1", int.class);

		for (ContentType contentType : HttpBinder.supportedContentTypes) {
			request.contentType(contentType);
			parameterDescriptions = map(param1, null);
			binder.bindAll(parameterDescriptions, request, response, pathVariables);
			assertThat(parameterDescriptions.get(param1), is((Object) 1));
		}
		for (ContentType contentType : list(ContentType.values()).removeItems(HttpBinder.supportedContentTypes)) {
			request.contentType(contentType);
			parameterDescriptions = map(param1, null);
			binder.bindAll(parameterDescriptions, request, response, pathVariables);
			assertThat(parameterDescriptions.get(param1), is(nullValue()));
		}
	}

	@Test
	public void shouldBindMultipleParams() {
		request.contentType(ContentType.Null);
		request.parameter("param1", "1");
		request.parameter("param2", "2");
		request.parameter("param3.value1", "3");
		request.parameter("param3.value2", "three");

		ParameterDescription param1 = new ParameterDescription("param1", int.class);
		ParameterDescription param2 = new ParameterDescription("param2", String.class);
		ParameterDescription param3 = new ParameterDescription("param3", TestBean.class);
		parameterDescriptions.put(param1, null);
		parameterDescriptions.put(param2, null);
		parameterDescriptions.put(param3, null);

		binder.bindAll(parameterDescriptions, request, response, pathVariables);
		assertThat(parameterDescriptions.get(param1), is((Object) 1));
		assertThat(parameterDescriptions.get(param2), is((Object) "2"));
		assertThat(parameterDescriptions.get(param3), is((Object) new TestBean("3", "three")));
	}

	public static class TestBean {
		private String value1;
		private String value2;

		public TestBean() {

		}

		public TestBean(String value1, String value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		public String getValue1() {
			return value1;
		}

		public void setValue1(String value1) {
			this.value1 = value1;
		}

		public String getValue2() {
			return value2;
		}

		public void setValue2(String value2) {
			this.value2 = value2;
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, false);
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj, false);
		}
	}
}
