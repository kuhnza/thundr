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

import static com.threewks.thundr.http.URLEncoder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

;
public class URLEncoderTest {

	@Test
	public void shouldEncodeQueryComponent() {
		// basic whitespace and empty values
		assertThat(encodeQueryComponent(null), is(nullValue()));
		assertThat(encodeQueryComponent(""), is(""));
		assertThat(encodeQueryComponent(" "), is("%20"));
		assertThat(encodeQueryComponent("  "), is("%20%20"));

		// regular sensible values
		assertThat(encodeQueryComponent("text"), is("text"));
		assertThat(encodeQueryComponent("text and more"), is("text%20and%20more"));

		// unreserved characters
		assertThat(encodeQueryComponent("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"), is("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"));

		// query reserved characters
		assertThat(encodeQueryComponent(";"), is("%3B"));
		assertThat(encodeQueryComponent("/"), is("%2F"));
		assertThat(encodeQueryComponent("?"), is("%3F"));
		assertThat(encodeQueryComponent(":"), is("%3A"));
		assertThat(encodeQueryComponent("@"), is("%40"));
		assertThat(encodeQueryComponent("&"), is("%26"));
		assertThat(encodeQueryComponent("="), is("%3D"));
		assertThat(encodeQueryComponent("+"), is("%2B"));
		assertThat(encodeQueryComponent(","), is("%2C"));
		assertThat(encodeQueryComponent(";"), is("%3B"));
		assertThat(encodeQueryComponent("#"), is("%23"));
		assertThat(encodeQueryComponent("$"), is("%24"));
		assertThat(encodeQueryComponent("%"), is("%25"));
		assertThat(encodeQueryComponent("^"), is("%5E"));
		assertThat(encodeQueryComponent("\\"), is("%5C"));
		assertThat(encodeQueryComponent("\""), is("%22"));
		assertThat(encodeQueryComponent(">"), is("%3E"));
		assertThat(encodeQueryComponent("<"), is("%3C"));
		assertThat(encodeQueryComponent(","), is("%2C"));
		assertThat(encodeQueryComponent("¡"), is("%A1"));
		assertThat(encodeQueryComponent("™"), is("%2122"));
		assertThat(encodeQueryComponent("£"), is("%A3"));
	}

	@Test
	public void shouldEncodePathComponent() {
		// basic whitespace and empty values
		assertThat(encodePathComponent(null), is(nullValue()));
		assertThat(encodePathComponent(""), is(""));
		assertThat(encodePathComponent(" "), is("%20"));
		assertThat(encodePathComponent("  "), is("%20%20"));

		// regular sensible values
		assertThat(encodePathComponent("text"), is("text"));
		assertThat(encodePathComponent("text and more"), is("text%20and%20more"));

		// unreserved characters
		assertThat(encodePathComponent("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"), is("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"));

		// path reserved characters
		assertThat(encodePathComponent("/"), is("%2F"));
		assertThat(encodePathComponent("?"), is("%3F"));
		assertThat(encodePathComponent(":"), is("%3A"));
		assertThat(encodePathComponent("@"), is("%40"));
		assertThat(encodePathComponent("&"), is("%26"));
		assertThat(encodePathComponent("="), is("%3D"));
		assertThat(encodePathComponent("+"), is("%2B"));
		assertThat(encodePathComponent(","), is("%2C"));
		assertThat(encodePathComponent(";"), is("%3B"));
		assertThat(encodePathComponent("#"), is("%23"));
		assertThat(encodePathComponent("%"), is("%25"));
		assertThat(encodePathComponent("^"), is("%5E"));
		assertThat(encodePathComponent("\\"), is("%5C"));
		assertThat(encodePathComponent("\""), is("%22"));
		assertThat(encodePathComponent(">"), is("%3E"));
		assertThat(encodePathComponent("<"), is("%3C"));
		assertThat(encodePathComponent("¡"), is("%A1"));
		assertThat(encodePathComponent("™"), is("%2122"));
		assertThat(encodePathComponent("£"), is("%A3"));
	}

	@Test
	public void shouldEncodePathSlugComponent() {
		// basic whitespace and empty values
		assertThat(encodePathSlugComponent(null), is(nullValue()));
		assertThat(encodePathSlugComponent(""), is(""));
		assertThat(encodePathSlugComponent(" "), is("-"));
		assertThat(encodePathSlugComponent("  "), is("-"));
		assertThat(encodePathSlugComponent("PathContent"), is("PathContent"));
		assertThat(encodePathSlugComponent("Path Content"), is("Path-Content"));
		assertThat(encodePathSlugComponent("Path&Content"), is("Path-Content"));
		assertThat(encodePathSlugComponent("Path & Content"), is("Path-Content"));
		assertThat(encodePathSlugComponent("Path, and Content"), is("Path-and-Content"));
		assertThat(encodePathSlugComponent("Path's and Content"), is("Paths-and-Content"));
	}

	@Test
	public void shouldDecodePath() {
		assertThat(decodePathComponent(encodePathComponent("This is - some, stuff & ? more things")), is("This is - some, stuff & ? more things"));
		assertThat(decodePathComponent(encodePathComponent("This is **)()()@#!898492834dfkajd fkjd><\":}{}- some, stuff & ? more things")), is("This is **)()()@#!898492834dfkajd fkjd><\":}{}- some, stuff & ? more things"));

	}

}
