package com.atomicleopard.webFramework.bind2;

import static com.atomicleopard.expressive.Expressive.array;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jodd.introspector.ClassDescriptor;

import org.junit.Test;

import com.atomicleopard.expressive.Expressive;

public class ActionBinderTest {
	private TestBindTo testBindTo = new TestBindTo();
	private Map<String, String[]> emptyMap = Collections.emptyMap();
	private Map<String, String[]> defaultMap = Expressive.map("argument1", array("value1"), "argument2", array("value2"), "argument3", array("value3"));
	private MethodParameterBinder binder = new MethodParameterBinder();
	private ClassDescriptor classDescriptor = new ClassDescriptor(TestBindTo.class, true);

	@Test
	public void shouldInvokeNone() throws Exception {
		Method method = classDescriptor.getAllMethods("methodNone")[0];
		assertThat((String) binder.invoke(testBindTo, method, emptyMap), is("none"));
		assertThat((String) binder.invoke(testBindTo, method, defaultMap), is("none"));
	}

	@Test
	public void shouldInvokeSingleString() throws Exception {
		Method method = classDescriptor.getAllMethods("methodSingleString")[0];
		assertThat((String) binder.invoke(testBindTo, method, emptyMap), is(nullValue()));
		assertThat((String) binder.invoke(testBindTo, method, defaultMap), is("value1"));
	}

	@Test
	public void shouldInvokeDoubleString() throws Exception {
		Method method = classDescriptor.getAllMethods("methodDoubleString")[0];
		assertThat((String) binder.invoke(testBindTo, method, emptyMap), is("null:null"));
		assertThat((String) binder.invoke(testBindTo, method, defaultMap), is("value1:value2"));
		assertThat((String) binder.invoke(testBindTo, method, map("argument1", "value1")), is("value1:null"));
		assertThat((String) binder.invoke(testBindTo, method, map("argument2", "value2")), is("null:value2"));
	}

	@Test
	public void shouldInvokeStringList() throws Exception {
		Method method = classDescriptor.getAllMethods("methodStringList")[0];
		assertThat((String) binder.invoke(testBindTo, method, emptyMap), is(""));
		assertThat((String) binder.invoke(testBindTo, method, map("argument1[0]", "value1")), is("value1"));
		Map<String, String[]> map = Expressive.map("argument1[0]", new String[]{"value1"}, "argument1[1]", new String[]{"value2"}, "argument1[3]", new String[]{"value3"});
		assertThat((String) binder.invoke(testBindTo, method, map), is("value1:value2::value3"));
		map = Expressive.map("argument1[0]", new String[]{""}, "argument1[1]", new String[]{"value2"}, "argument1[3]", new String[]{"value3"});
		assertThat((String) binder.invoke(testBindTo, method, map), is(":value2::value3"));
		assertThat((String) binder.invoke(testBindTo, method, map("argument1", "value1", "value2")), is("value1:value2"));
	}

	private Map<String, String[]> map(String key, String... values) {
		Map<String, String[]> map = Expressive.map(key, values);
		return map;
	}
}
