package com.atomicleopard.webFramework.matchers;

import static com.atomicleopard.expressive.Expressive.list;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;

public class IsArrayOfMatcher<T> extends BaseMatcher<T[]> {
	private EList<T> expected;
	private Class<T> type;

	public IsArrayOfMatcher(Class<T> type, EList<T> expected) {
		super();
		this.expected = expected;
		this.type = type;

	}

	@Override
	public boolean matches(Object arg0) {
		Object[] as = Cast.as(arg0, Object[].class);
		if (expected.size() != as.length) {
			return false;
		}
		for (int i = 0; i < expected.size(); i++) {
			Object actualValue = as[i];
			Object expectedValue = expected.at(i);
			if (expectedValue == null) {
				if (actualValue != null) {
					return false;
				}
			} else if (!expectedValue.equals(actualValue)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void describeTo(Description arg0) {
		Matchers.is(expected).describeTo(arg0);
	}

	public static <T> IsArrayOfMatcher<T> isArray(Class<T> type, T... items) {
		return new IsArrayOfMatcher<T>(type, list(items));
	}
}
