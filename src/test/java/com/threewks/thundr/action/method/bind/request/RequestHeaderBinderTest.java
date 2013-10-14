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
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class RequestHeaderBinderTest {

	private RequestHeaderBinder binder = new RequestHeaderBinder();
	private Map<ParameterDescription, Object> parameterDescriptions;
	private HashMap<String, String> pathVariables;
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Before
	public void before() {
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
		ParameterDescription param1 = new ParameterDescription("param1", new ArrayList<String>() {
		}.getClass().getGenericSuperclass());
		ParameterDescription param2 = new ParameterDescription("param2", new HashSet<Integer>() {
		}.getClass().getGenericSuperclass());
		ParameterDescription param3 = new ParameterDescription("param3", ((Collection<String>) new ArrayList<String>() {
		}).getClass().getGenericSuperclass());

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

	@Test
	public void shouldNormaliseHeaderNamesToAvoidContainerDependency() {
		ParameterDescription param1 = new ParameterDescription("xHttpHeaderParam1", String.class);
		ParameterDescription param2 = new ParameterDescription("xHttpHeaderParam2", int.class);
		ParameterDescription param3 = new ParameterDescription("xHttpHeaderParam3", Integer.class);
		ParameterDescription param4 = new ParameterDescription("xHttpHeaderParam4", double.class);
		ParameterDescription param5 = new ParameterDescription("xHttpHeaderParam5", Double.class);
		ParameterDescription param6 = new ParameterDescription("xHttpHeaderParam6", short.class);
		ParameterDescription param7 = new ParameterDescription("xHttpHeaderParam7", Short.class);
		ParameterDescription param8 = new ParameterDescription("xHttpHeaderParam8", float.class);
		ParameterDescription param9 = new ParameterDescription("xHttpHeaderParam9", Float.class);
		ParameterDescription param10 = new ParameterDescription("xHttpHeaderParam10", long.class);
		ParameterDescription param11 = new ParameterDescription("xHttpHeaderParam11", Long.class);
		ParameterDescription param12 = new ParameterDescription("xHttpHeaderParam12", BigDecimal.class);
		ParameterDescription param13 = new ParameterDescription("xHttpHeaderParam13", BigInteger.class);

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

		request.header("X-Http-Header-Param1", "string-value");
		request.header("x-http-header-param2", "2");
		request.header("X-HTTP-HEADER-PARAM3", "3");
		request.header("X-Http-Header-Param4", "4.0");
		request.header("X-Http-Header-Param5", "5.0");
		request.header("X-Http-Header-Param6", "6");
		request.header("X-Http-Header-Param7", "7");
		request.header("X-Http-Header-Param8", "8.8");
		request.header("X-Http-Header-Param9", "9.9");
		request.header("X-Http-Header-Param10", "10");
		request.header("X-Http-Header-Param11", "11");
		request.header("X-Http-Header-Param12", "12.00");
		request.header("X-Http-Header-Param13", "13");

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
}
