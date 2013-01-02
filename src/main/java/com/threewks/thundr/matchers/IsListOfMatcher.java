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
