package com.threewks.thundr.matchers;

public class ExtendedMatchers {

	public static <T> IsListOfMatcher<T> isList(Class<T> type, T... items) {
		return IsListOfMatcher.isList(items);
	}
	
	public static <T> IsArrayOfMatcher<T> isArray(Class<T> type, T... items) {
		return IsArrayOfMatcher.isArray(type, items);
	}
}
