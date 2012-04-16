package com.atomicleopard.webFramework.bind;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class TestBindTo {
	public Object methodNone() {
		return "none";
	}

	public Object methodSingleString(String argument1) {
		return argument1;
	}

	public Object methodDoubleString(String argument1, String argument2) {
		return argument1 + ":" + argument2;
	}

	public Object methodStringList(List<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringSet(Set<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringCollection(Collection<String> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodStringArray(String[] argument1) {
		return StringUtils.join(argument1, ":");
	}

	public Object methodMap(Map<String, List<String>> argument1) {
		return StringUtils.join(argument1, ":");
	}

	public <T extends String> Object methodGenericArray(T[] argument1) {
		return StringUtils.join(argument1, ":");
	}

	public JavaBean methodJavaBean(JavaBean argument1) {
		return argument1;
	}
	
	public DeepJavaBean methodDeepJavaBean(DeepJavaBean argument1) {
		return argument1;
	}
}
