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
package com.threewks.thundr.action;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ActionExceptionTest {

	@Test
	public void shouldRetainMessage() {
		ActionException actionException = new ActionException("Format %s", "message");
		assertThat(actionException.getMessage(), is("Format message"));
		assertThat(actionException.getCause(), is(nullValue()));
	}

	@Test
	public void shouldRetainMessageAndCause() {
		Throwable cause = new RuntimeException();
		ActionException actionException = new ActionException(cause, "Format %s", "message");
		assertThat(actionException.getMessage(), is("Format message"));
		assertThat(actionException.getCause(), is(cause));
	}
}
