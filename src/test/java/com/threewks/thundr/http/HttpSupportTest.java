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
package com.threewks.thundr.http;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HttpSupportTest {

	@Test
	public void shouldReturnReasonForStatusCode() {
		assertThat(HttpSupport.getReasonForHttpStatus(200), is("OK"));
		assertThat(HttpSupport.getReasonForHttpStatus(400), is("Bad Request"));
		assertThat(HttpSupport.getReasonForHttpStatus(401), is("Unauthorized"));
		assertThat(HttpSupport.getReasonForHttpStatus(500), is("Internal Server Error"));
		assertThat(HttpSupport.getReasonForHttpStatus(418), is("I'm a teapot"));
	}
}
