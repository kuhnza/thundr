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

	@Test
	public void shouldReturnTrueForMatchingHttpMethod() {
		assertThat(HttpSupport.Methods.isGet("get"), is(true));
		assertThat(HttpSupport.Methods.isGet("GeT"), is(true));
		assertThat(HttpSupport.Methods.isGet("GET"), is(true));
		assertThat(HttpSupport.Methods.isGet(null), is(false));
		assertThat(HttpSupport.Methods.isGet(""), is(false));
		assertThat(HttpSupport.Methods.isGet(" "), is(false));
		assertThat(HttpSupport.Methods.isGet(" get "), is(false));
		assertThat(HttpSupport.Methods.isGet("POST"), is(false));

		assertThat(HttpSupport.Methods.isPost("post"), is(true));
		assertThat(HttpSupport.Methods.isPost("PoSt"), is(true));
		assertThat(HttpSupport.Methods.isPost("POST"), is(true));
		assertThat(HttpSupport.Methods.isPost(null), is(false));
		assertThat(HttpSupport.Methods.isPost(""), is(false));
		assertThat(HttpSupport.Methods.isPost(" "), is(false));
		assertThat(HttpSupport.Methods.isPost(" post "), is(false));

		assertThat(HttpSupport.Methods.isPut("put"), is(true));
		assertThat(HttpSupport.Methods.isPut("pUt"), is(true));
		assertThat(HttpSupport.Methods.isPut("PUT"), is(true));
		assertThat(HttpSupport.Methods.isPut(null), is(false));
		assertThat(HttpSupport.Methods.isPut(""), is(false));
		assertThat(HttpSupport.Methods.isPut(" "), is(false));
		assertThat(HttpSupport.Methods.isPut(" put "), is(false));

		assertThat(HttpSupport.Methods.isPatch("patch"), is(true));
		assertThat(HttpSupport.Methods.isPatch("pAtCh"), is(true));
		assertThat(HttpSupport.Methods.isPatch("PATCH"), is(true));
		assertThat(HttpSupport.Methods.isPatch(null), is(false));
		assertThat(HttpSupport.Methods.isPatch(""), is(false));
		assertThat(HttpSupport.Methods.isPatch(" "), is(false));
		assertThat(HttpSupport.Methods.isPatch(" patch "), is(false));

		assertThat(HttpSupport.Methods.isDelete("delete"), is(true));
		assertThat(HttpSupport.Methods.isDelete("DeLEtE"), is(true));
		assertThat(HttpSupport.Methods.isDelete("DELETE"), is(true));
		assertThat(HttpSupport.Methods.isDelete(null), is(false));
		assertThat(HttpSupport.Methods.isDelete(""), is(false));
		assertThat(HttpSupport.Methods.isDelete(" "), is(false));
		assertThat(HttpSupport.Methods.isDelete(" delete "), is(false));
	}
}
