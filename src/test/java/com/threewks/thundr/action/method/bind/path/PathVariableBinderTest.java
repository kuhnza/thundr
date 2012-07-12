package com.threewks.thundr.action.method.bind.path;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.threewks.thundr.introspection.ParameterDescription;

public class PathVariableBinderTest {

	private PathVariableBinder pathVariableBinder;
	private List<ParameterDescription> parameterDescriptions;
	private HashMap<String, String> pathVariables;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@Before
	public void before() {
		pathVariableBinder = new PathVariableBinder();
		parameterDescriptions = new ArrayList<ParameterDescription>();
		pathVariables = new HashMap<String, String>();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	@Test
	public void shouldHandleCoreTypeParamBindings() {
		parameterDescriptions.add(new ParameterDescription("param1", String.class));
		parameterDescriptions.add(new ParameterDescription("param2", int.class));
		parameterDescriptions.add(new ParameterDescription("param3", Integer.class));
		parameterDescriptions.add(new ParameterDescription("param4", double.class));
		parameterDescriptions.add(new ParameterDescription("param5", Double.class));
		parameterDescriptions.add(new ParameterDescription("param6", short.class));
		parameterDescriptions.add(new ParameterDescription("param7", Short.class));
		parameterDescriptions.add(new ParameterDescription("param8", float.class));
		parameterDescriptions.add(new ParameterDescription("param9", Float.class));
		parameterDescriptions.add(new ParameterDescription("param10", BigDecimal.class));
		parameterDescriptions.add(new ParameterDescription("param11", BigInteger.class));

		pathVariables.put("param1", "string-value");
		pathVariables.put("param2", "2");
		pathVariables.put("param3", "3");
		pathVariables.put("param4", "4.0");
		pathVariables.put("param5", "5.0");
		pathVariables.put("param6", "6");
		pathVariables.put("param7", "7");
		pathVariables.put("param8", "8.8");
		pathVariables.put("param9", "9.9");
		pathVariables.put("param10", "10.00");
		pathVariables.put("param11", "11");

		List<Object> bindedResults = pathVariableBinder.bindAll(parameterDescriptions, request, response, pathVariables);
		Assert.assertThat(bindedResults.size(), is(parameterDescriptions.size()));
		Iterator<ParameterDescription> iterator = parameterDescriptions.iterator();
		for (Object result : bindedResults) {
			ParameterDescription next = iterator.next();
			Assert.assertNotNull(String.format("failed to convert %s", next.name()), result);
		}
	}
}
