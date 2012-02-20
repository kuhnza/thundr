package com.atomicleopard.webFramework.bind;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jodd.bean.BeanTool;
import jodd.bean.BeanUtil;

import org.junit.Test;

public class SetBeanPropertyTest {

	@Test
	public void shouldCopyFromMapToMapParameters() {
		Map<String, String> fakeRequest = mapKeys("name.value", "name.list[0]", "list[0]", "list[1]","string").to("Value1", "Hi", "first", "second", "and?");
		//Map<String, String> fakeRequest = mapKeys("name.value").to("Value1");
		MyPojo myPojo = new MyPojo();
		List<String> myList = new ArrayList<String>();
		Map<String, Object> destination = map("name", myPojo, "list", myList, "string", null);

		Map<String, String> source = fakeRequest;
		//BeanTool.copy(fakeRequest, myPojo);
		String[] properties = BeanTool.resolveProperties(source, false);
		for (String name : properties) {
			Object value = source.get(name);
			BeanUtil.setPropertyForcedSilent(destination, name, value);
		}

		assertThat(myPojo.getValue(), is("Value1"));
		assertThat(myPojo.getList().size(), is(1));
		assertThat(myPojo.getList().get(0), is("Hi"));
		assertThat(myList.get(0), is("first"));
		assertThat(myList.get(1), is("second"));
		assertThat(destination.get("string"), is(notNullValue()));
		assertThat((String)destination.get("string"), is("and?"));

	}

	public static class MyPojo {
		private String value;
		private List<String> list;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}
	}
}
