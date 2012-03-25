package com.atomicleopard.webFramework.matchers;

import static com.atomicleopard.expressive.Expressive.list;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;

import com.atomicleopard.expressive.EList;

public class IsListOfMatcher<T> extends BaseMatcher<List<T>> {
	private EList<T> expected;

	public IsListOfMatcher(EList<T> expected) {
		super();
		this.expected = expected;
	}

	@Override
	public boolean matches(Object arg0) {
		return Matchers.is(expected).matches(arg0);
	}

	@Override
	public void describeTo(Description arg0) {
		Matchers.is(expected).describeTo(arg0);
	}

	public static <T> IsListOfMatcher<T> isList(T... items) {
		return new IsListOfMatcher<T>(list(items));
	}
}
