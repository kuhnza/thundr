package com.threewks.thundr.action.method.bind.request;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.action.method.bind.request.RequestHeaderBinder;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class RequestHeaderBinderTest {

	private RequestHeaderBinder binder;
	private Map<ParameterDescription, Object> parameterDescriptions;
	private HashMap<String, String> pathVariables;
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Before
	public void before() {
		binder = new RequestHeaderBinder(new HttpBinder());
		parameterDescriptions = new LinkedHashMap<ParameterDescription, Object>();
		pathVariables = new HashMap<String, String>();
	}

	@Test
	public void shouldHandleCoreTypeParamBindings() {
		ParameterDescription param1 = new ParameterDescription("param1", String.class);
		ParameterDescription param2 = new ParameterDescription("param2", int.class);
		ParameterDescription param3 = new ParameterDescription("param3", Integer.class);
		ParameterDescription param4 = new ParameterDescription("param4", double.class);
		ParameterDescription param5 = new ParameterDescription("param5", Double.class);
		ParameterDescription param6 = new ParameterDescription("param6", short.class);
		ParameterDescription param7 = new ParameterDescription("param7", Short.class);
		ParameterDescription param8 = new ParameterDescription("param8", float.class);
		ParameterDescription param9 = new ParameterDescription("param9", Float.class);
		ParameterDescription param10 = new ParameterDescription("param10", long.class);
		ParameterDescription param11 = new ParameterDescription("param11", Long.class);
		ParameterDescription param12 = new ParameterDescription("param12", BigDecimal.class);
		ParameterDescription param13 = new ParameterDescription("param13", BigInteger.class);

		parameterDescriptions.put(param1, null);
		parameterDescriptions.put(param2, null);
		parameterDescriptions.put(param3, null);
		parameterDescriptions.put(param4, null);
		parameterDescriptions.put(param5, null);
		parameterDescriptions.put(param6, null);
		parameterDescriptions.put(param7, null);
		parameterDescriptions.put(param8, null);
		parameterDescriptions.put(param9, null);
		parameterDescriptions.put(param10, null);
		parameterDescriptions.put(param11, null);
		parameterDescriptions.put(param12, null);
		parameterDescriptions.put(param13, null);

		request.header("param1", "string-value");
		request.header("param2", "2");
		request.header("param3", "3");
		request.header("param4", "4.0");
		request.header("param5", "5.0");
		request.header("param6", "6");
		request.header("param7", "7");
		request.header("param8", "8.8");
		request.header("param9", "9.9");
		request.header("param10", "10");
		request.header("param11", "11");
		request.header("param12", "12.00");
		request.header("param13", "13");

		binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(parameterDescriptions.get(param1), is((Object) "string-value"));
		assertThat(parameterDescriptions.get(param2), is((Object) 2));
		assertThat(parameterDescriptions.get(param3), is((Object) 3));
		assertThat(parameterDescriptions.get(param4), is((Object) 4.0));
		assertThat(parameterDescriptions.get(param5), is((Object) 5.0));
		assertThat(parameterDescriptions.get(param6), is((Object) (short) 6));
		assertThat(parameterDescriptions.get(param7), is((Object) (short) 7));
		assertThat(parameterDescriptions.get(param8), is((Object) 8.8f));
		assertThat(parameterDescriptions.get(param9), is((Object) 9.9f));
		assertThat(parameterDescriptions.get(param10), is((Object) 10L));
		assertThat(parameterDescriptions.get(param11), is((Object) 11L));
		assertThat(parameterDescriptions.get(param12), is((Object) new BigDecimal("12.00")));
		assertThat(parameterDescriptions.get(param13), is((Object) BigInteger.valueOf(13)));
	}

	@Test
	public void shouldLeaveUnbindableValuesNull() {
		ParameterDescription param1 = new ParameterDescription("param1", String.class);
		ParameterDescription param2 = new ParameterDescription("param2", UUID.class);
		ParameterDescription param3 = new ParameterDescription("param3", Object.class);

		parameterDescriptions.put(param1, null);
		parameterDescriptions.put(param2, null);

		request.header("param1", "string-value");
		request.header("param2", UUID.randomUUID().toString());
		request.header("param3", "3");

		binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(parameterDescriptions.get(param1), is((Object) "string-value"));
		assertThat(parameterDescriptions.get(param2), is(nullValue()));
		assertThat(parameterDescriptions.get(param3), is(nullValue()));
	}

	@SuppressWarnings("serial")
	@Test
	public void shouldBindHeaderValuesToCollections() {
		ParameterDescription param1 = new ParameterDescription("param1", new ArrayList<String>() {}.getClass().getGenericSuperclass());
		ParameterDescription param2 = new ParameterDescription("param2", new HashSet<Integer>() {}.getClass().getGenericSuperclass());
		ParameterDescription param3 = new ParameterDescription("param3", ((Collection<String>)new ArrayList<String>() {}).getClass().getGenericSuperclass());

		parameterDescriptions.put(param1, null);
		parameterDescriptions.put(param2, null);
		parameterDescriptions.put(param3, null);

		request.header("param1", "1", "one");
		request.header("param2", "2", "22");
		request.header("param3", "3", "three");

		binder.bindAll(parameterDescriptions, request, response, pathVariables);

		assertThat(parameterDescriptions.get(param1), is((Object) list("1", "one")));
		assertThat(parameterDescriptions.get(param2), is((Object) set(2, 22)));
		assertThat(parameterDescriptions.get(param3), is((Object) list("3", "three")));
	}

	public List<String> getStringList() {
		return null;
	}

}
