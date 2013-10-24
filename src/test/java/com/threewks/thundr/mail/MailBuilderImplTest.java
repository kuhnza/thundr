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
package com.threewks.thundr.mail;

import static com.atomicleopard.expressive.Expressive.map;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;

public class MailBuilderImplTest {

	private Mailer mailer = mock(Mailer.class);
	private MockHttpServletRequest req = new MockHttpServletRequest();
	private MailBuilderImpl builder = new MailBuilderImpl(mailer, req);

	@Test
	public void shouldRetainSubject() {
		assertThat(builder.subject(), is(nullValue()));
		builder.subject("Subject");
		assertThat(builder.subject(), is("Subject"));
	}

	@Test
	public void shouldRetainFrom() {
		assertThat(builder.from(), is(nullValue()));

		builder.from("email@address.com");
		assertThat(builder.from(), is(entry("email@address.com")));

		builder.from("email@address.com", "Emailer");
		assertThat(builder.from(), is(entry("email@address.com", "Emailer")));
	}

	@Test
	public void shouldRetainReplyTo() {
		assertThat(builder.replyTo(), is(nullValue()));

		builder.replyTo("email@address.com");
		assertThat(builder.replyTo(), is(entry("email@address.com")));

		builder.replyTo("email@address.com", "Emailer");
		assertThat(builder.replyTo(), is(entry("email@address.com", "Emailer")));
	}

	@Test
	public void shouldAddToRecipients() {
		assertThat(builder.to().isEmpty(), is(true));
		builder.to("test@email1.com");
		assertThat(builder.to(), hasEntry("test@email1.com", null));

		builder.to("test@email2.com", "Email Two");
		assertThat(builder.to(), hasEntry("test@email1.com", null));
		assertThat(builder.to(), hasEntry("test@email2.com", "Email Two"));

		Map<String, String> emails = map("test@email3.com", "Email Three", "test@email4.com", "Email Four");
		builder.to(emails);
		assertThat(builder.to(), hasEntry("test@email1.com", null));
		assertThat(builder.to(), hasEntry("test@email2.com", "Email Two"));
		assertThat(builder.to(), hasEntry("test@email3.com", "Email Three"));
		assertThat(builder.to(), hasEntry("test@email4.com", "Email Four"));
	}

	@Test
	public void shouldAddCcRecipients() {
		assertThat(builder.cc().isEmpty(), is(true));
		builder.cc("test@email1.com");
		assertThat(builder.cc(), hasEntry("test@email1.com", null));

		builder.cc("test@email2.com", "Email Two");
		assertThat(builder.cc(), hasEntry("test@email1.com", null));
		assertThat(builder.cc(), hasEntry("test@email2.com", "Email Two"));

		Map<String, String> emails = map("test@email3.com", "Email Three", "test@email4.com", "Email Four");
		builder.cc(emails);
		assertThat(builder.cc(), hasEntry("test@email1.com", null));
		assertThat(builder.cc(), hasEntry("test@email2.com", "Email Two"));
		assertThat(builder.cc(), hasEntry("test@email3.com", "Email Three"));
		assertThat(builder.cc(), hasEntry("test@email4.com", "Email Four"));
	}

	@Test
	public void shouldAddBccRecipients() {
		assertThat(builder.bcc().isEmpty(), is(true));
		builder.bcc("test@email1.com");
		assertThat(builder.bcc(), hasEntry("test@email1.com", null));

		builder.bcc("test@email2.com", "Email Two");
		assertThat(builder.bcc(), hasEntry("test@email1.com", null));
		assertThat(builder.bcc(), hasEntry("test@email2.com", "Email Two"));

		Map<String, String> emails = map("test@email3.com", "Email Three", "test@email4.com", "Email Four");
		builder.bcc(emails);
		assertThat(builder.bcc(), hasEntry("test@email1.com", null));
		assertThat(builder.bcc(), hasEntry("test@email2.com", "Email Two"));
		assertThat(builder.bcc(), hasEntry("test@email3.com", "Email Three"));
		assertThat(builder.bcc(), hasEntry("test@email4.com", "Email Four"));
	}

	@Test
	public void shouldRetainEmailBody() {
		assertThat(builder.body(), is(nullValue()));

		builder.body("String");

		assertThat(builder.<String> body(), is("String"));
	}

	@Test
	public void shouldRetainRequest() {
		assertThat(builder.request(), is(sameInstance((HttpServletRequest) req)));
	}

	@Test
	public void shouldInvokeMailerOnSend() {
		builder.send();
		verify(mailer, times(1)).send(builder);
	}

	public static final Map.Entry<String, String> entry(String email) {
		return Collections.singletonMap(email, (String) null).entrySet().iterator().next();
	}

	public static final Map.Entry<String, String> entry(String email, String name) {
		return Collections.singletonMap(email, name).entrySet().iterator().next();
	}
}
