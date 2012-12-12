package com.threewks.thundr.action.method.bind.request;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.threewks.thundr.action.method.bind.http.HttpBinder;
import com.threewks.thundr.introspection.ParameterDescription;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.test.mock.servlet.MockHttpServletResponse;

public class RequestAttributeBinderTest {
	private RequestAttributeBinder binder = new RequestAttributeBinder(new HttpBinder());
	private MockHttpServletRequest req = new MockHttpServletRequest();
	private HttpServletResponse resp = new MockHttpServletResponse();
	private Map<String, String> pathVariables = map();
	private Map<ParameterDescription, Object> bindings = map();

	@Test
	public void shouldBindRequestAttributeMatchingParameterName() {
		ParameterDescription varParam = new ParameterDescription("var", String.class);
		bindings.put(varParam, null);
		req.setAttribute("var", "expected");
		binder.bindAll(bindings, req, resp, pathVariables);

		assertThat(bindings.get(varParam), is((Object) "expected"));
	}

	@Test
	public void shouldOnlyBindRequestAttributeWhenNoBindingAlreadyMade() {
		ParameterDescription varParam = new ParameterDescription("var", String.class);
		bindings.put(varParam, "original");
		req.setAttribute("var", "overridden");
		binder.bindAll(bindings, req, resp, pathVariables);

		assertThat(bindings.get(varParam), is((Object) "original"));
	}

	@Test
	public void shouldSupportTypeBindingOfStringsByRelyingOnHttpBinder() {
		ParameterDescription varParam = new ParameterDescription("var", Integer.class);
		bindings.put(varParam, null);
		req.setAttribute("var", "123");
		binder.bindAll(bindings, req, resp, pathVariables);

		assertThat(bindings.get(varParam), is((Object) 123));
	}

	@Test
	public void shouldBindMultipleRequestAttributes() {
		ParameterDescription varParam1 = new ParameterDescription("var1", String.class);
		ParameterDescription varParam2 = new ParameterDescription("var2", String.class);
		ParameterDescription varParam3 = new ParameterDescription("var3", String.class);
		bindings.put(varParam1, null);
		bindings.put(varParam2, null);
		bindings.put(varParam3, null);
		req.setAttribute("var1", "first");
		req.setAttribute("var2", "second");
		req.setAttribute("var3", "third");
		binder.bindAll(bindings, req, resp, pathVariables);

		assertThat(bindings.get(varParam1), is((Object) "first"));
		assertThat(bindings.get(varParam2), is((Object) "second"));
		assertThat(bindings.get(varParam3), is((Object) "third"));
	}

	@Test
	public void shouldBindRequestAttributesOfMatchingTypesDirectly() {
		ParameterDescription varParam1 = new ParameterDescription("integer", Integer.class);
		ParameterDescription varParam2 = new ParameterDescription("date", Date.class);
		ParameterDescription varParam3 = new ParameterDescription("string", String.class);
		bindings.put(varParam1, null);
		bindings.put(varParam2, null);
		bindings.put(varParam3, null);
		req.setAttribute("integer", 123);
		req.setAttribute("date", new Date(1));
		req.setAttribute("string", "stringVal");
		binder.bindAll(bindings, req, resp, pathVariables);

		assertThat(bindings.get(varParam1), is((Object) 123));
		assertThat(bindings.get(varParam2), is((Object) new Date(1)));
		assertThat(bindings.get(varParam3), is((Object) "stringVal"));
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

		Map<ParameterDescription, Object> parameterDescriptions = map();
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

		req.attribute("param1", "string-value");
		req.attribute("param2", "2");
		req.attribute("param3", "3");
		req.attribute("param4", "4.0");
		req.attribute("param5", "5.0");
		req.attribute("param6", "6");
		req.attribute("param7", "7");
		req.attribute("param8", "8.8");
		req.attribute("param9", "9.9");
		req.attribute("param10", "10");
		req.attribute("param11", "11");
		req.attribute("param12", "12.00");
		req.attribute("param13", "13");

		binder.bindAll(parameterDescriptions, req, resp, pathVariables);

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
