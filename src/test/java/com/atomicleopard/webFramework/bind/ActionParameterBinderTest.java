package com.atomicleopard.webFramework.bind;
import static java.util.Collections.*;
import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.atomicleopard.webFramework.routes.ActionMethod;

public class ActionParameterBinderTest {
	private Map<String, String> emptyMap = Collections.emptyMap();
	private ActionParameterBinder binder = new ActionParameterBinder();

	private HttpServletRequest request;

	@Before
	public void before() {
		request = mock(HttpServletRequest.class);
		when(request.getParameterMap()).thenReturn(emptyMap);
	}

	@Test
	public void shouldInvokeNone() throws Exception {
		assertThat(binder.bind(method("methodNone"), request, null, emptyMap).size(), is(0));
		assertThat(binder.bind(method("methodNone"), request("argument1", "value1"), null, null).size(), is(0));
	}

	private ActionMethod method(String method) throws ClassNotFoundException {
		ActionMethod actionMethod = new ActionMethod(String.format("com.atomicleopard.webFramework.bind2.TestBindTo.%s", method));
		return actionMethod;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldInvokeSingleString() throws Exception {
		assertThat(binder.bind(method("methodSingleString"), request, null, emptyMap), hasItems(nullValue()));
		assertThat(binder.bind(method("methodSingleString"), request("argument1", "value1"), null, null), hasItems((Object) "value1"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldInvokeDoubleString() throws Exception {
		assertThat(binder.bind(method("methodDoubleString"), request, null, emptyMap), hasItems(nullValue(), nullValue()));
		assertThat(binder.bind(method("methodDoubleString"), request("argument1", "value1", "argument2", "value2"), null, emptyMap), hasItems((Object) "value1", (Object) "value2"));
		assertThat(binder.bind(method("methodDoubleString"), request("argument1", "value1"), null, emptyMap), is((Object) list("value1", null)));
		assertThat(binder.bind(method("methodDoubleString"), request("argument2", "value2"), null, emptyMap), is((Object) list(null, "value2")));
	}

	@Test
	public void shouldInvokeStringList() throws Exception {
		assertThat(binder.bind(method("methodStringList"), request, null, emptyMap).size(), is(1));
		assertThat(binder.bind(method("methodStringList"), request, null, emptyMap), is((Object)singletonList(null)));
		assertThat(binder.bind(method("methodStringList"), request("argument1[0]", "value1"), null, emptyMap), hasItems((Object) list("value1")));
		assertThat(binder.bind(method("methodStringList"), request("argument1[0]", "value1", "argument1[1]", "value2", "argument1[3]", "value3"), null, emptyMap),
				hasItems((Object) list("value1", "value2", "value3")));
		assertThat(binder.bind(method("methodStringList"), request("argument1[0]", "", "argument1[1]", "value2", "argument1[3]", "value3"), null, emptyMap),
				hasItems((Object) list("", "value2", null, "value3")));
		assertThat(binder.bind(method("methodStringList"), request("argument1", new String[] { "value1", "value2" }), null, emptyMap), hasItems((Object) list("value1", "value2")));
	}

	private HttpServletRequest request(String... args) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < args.length; i += 2) {
			map.put(args[i], new String[] { args[i + 1] });
		}
		when(request.getParameterMap()).thenReturn(map);
		return request;
	}

	private HttpServletRequest request(String name, String[] values) {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put(name, values);
		when(request.getParameterMap()).thenReturn(map);
		return request;
	}
}
