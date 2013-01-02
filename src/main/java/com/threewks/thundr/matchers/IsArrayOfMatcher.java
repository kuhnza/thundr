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
package com.threewks.thundr.matchers;

import static com.atomicleopard.expressive.Expressive.list;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;

public class IsArrayOfMatcher<T> extends BaseMatcher<T[]> {
	private EList<T> expected;
	@SuppressWarnings("unused")
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
