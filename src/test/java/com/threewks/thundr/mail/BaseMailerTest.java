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

import static com.threewks.thundr.mail.MailBuilderImplTest.entry;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.exception.BaseException;
import com.threewks.thundr.test.mock.servlet.MockHttpServletRequest;
import com.threewks.thundr.view.ViewResolver;
import com.threewks.thundr.view.ViewResolverRegistry;
import com.threewks.thundr.view.string.StringView;
import com.threewks.thundr.view.string.StringViewResolver;

public class BaseMailerTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	private ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
	private BaseMailer mailer = spy(new BaseMailer(viewResolverRegistry) {
		@Override
		protected void sendInternal(Entry<String, String> from, Entry<String, String> replyTo, Map<String, String> to, Map<String, String> cc, Map<String, String> bcc, String subject, String content,
				String contentType) {
		}
	});
	private MockHttpServletRequest req = new MockHttpServletRequest();

	@Before
	public void before() throws MessagingException {
		viewResolverRegistry.addResolver(StringView.class, new StringViewResolver());
	}

	@Test
	public void shouldSendEmailusingJavaMailWithEmailFields() throws MessagingException {
		MailBuilder builder = mailer.mail(req);
		builder.from("test@email.com").replyTo("reply@email.com").to("recipient@email.com").cc("cc@email.com").bcc("bcc@email.com");
		builder.body(new StringView("Email body").contentType("text/plain"));
		builder.subject("Subject");
		builder.send();

		verify(mailer).send(builder);
		verify(mailer).sendInternal(entry("test@email.com"), entry("reply@email.com"), email("recipient@email.com"), email("cc@email.com"), email("bcc@email.com"), "Subject", "Email body",
				"text/plain");
	}

	@Test
	public void shouldSendBasicEmailUsingNames() throws MessagingException {
		MailBuilder builder = mailer.mail(req);
		Map<String, String> to = Expressive.map("steve@place.com", "Steve", "john@place.com", "John");
		builder.from("sender@email.com", "System Name");
		builder.to(to);
		builder.body(new StringView("Email body").contentType("text/plain"));
		builder.subject("Subject line");
		builder.send();

		verify(mailer).send(builder);
		verify(mailer).sendInternal(entry("sender@email.com", "System Name"), null, to, empty(), empty(), "Subject line", "Email body", "text/plain");
	}

	@Test
	public void shouldThrownMailExceptionIfSendInternalFails() throws MessagingException {
		thrown.expect(MailException.class);
		thrown.expectMessage("Failed to send an email: expected message");

		doThrow(new RuntimeException("expected message")).when(mailer).sendInternal(anyEntry(), anyEntry(), anyMap(), anyMap(), anyMap(), anyString(), anyString(), anyString());

		MailBuilder builder = mailer.mail(req);
		builder.from("sender@email.com", "System Name");
		builder.to("steve@place.com", "Steve");
		builder.body(new StringView("Email body").contentType("text/plain"));
		builder.subject("Subject line");
		builder.send();

		verify(mailer).sendInternal(entry("sender@email.com", "System Name"), null, email("steve@place.com", "Steve"), empty(), empty(), "Subject line", "Email body", "text/plain");
	}

	@Test
	public void shouldThrownMailExceptionIfFromIsMissing() throws MessagingException {
		thrown.expect(MailException.class);
		thrown.expectMessage("No sender has been set for this email");

		MailBuilder builder = mailer.mail(req);
		builder.to("steve@place.com", "Steve");
		builder.send();
	}

	@Test
	public void shouldThrownMailExceptionIfFromEmailIsNull() throws MessagingException {
		thrown.expect(MailException.class);
		thrown.expectMessage("No sender has been set for this email");

		MailBuilder builder = mailer.mail(req);
		builder.from(null);
		builder.to("steve@place.com", "Steve");
		builder.send();
	}

	@Test
	public void shouldThrownMailExceptionIfNoRecipientIsDefined() throws MessagingException {
		thrown.expect(MailException.class);
		thrown.expectMessage("No recipient (to, cc or bcc) has been set for this email");

		MailBuilder builder = mailer.mail(req);
		builder.from("sender@place.com", "Steve");
		builder.body(new StringView("Email body").contentType("text/plain"));
		builder.subject("Subject line");
		builder.send();
	}

	@Test
	public void shouldThrowMailExceptionIfViewResolutionFails() {
		thrown.expect(MailException.class);
		thrown.expectMessage("Failed to render email body: null");

		MailBuilder builder = mailer.mail(req);
		builder.from("junk");
		builder.to("steve", "Steve");
		builder.body("No resolver registered for strings");
		builder.subject("Subject line");
		builder.send();
	}

	@Test
	public void shouldDefaultContentTypeToTextHtmlIfNonePresent() {
		MailBuilder builder = mailer.mail(req);
		builder.from("sender@email.com");
		builder.to("recipient@email.com");
		builder.body(new StringView("Email body").contentType(null));
		builder.subject("Subject line");
		builder.send();

		verify(mailer).sendInternal(entry("sender@email.com"), null, email("recipient@email.com"), empty(), empty(), "Subject line", "Email body", "text/html");
	}

	@Test
	public void shouldNotFailToRenderBodyIfNoHttpServletRequestSupplied() {
		MailBuilder builder = mailer.mail(null);
		builder.from("sender@email.com");
		builder.to("recipient@email.com");
		builder.body(new StringView("Email body").contentType(null));
		builder.subject("Subject line");
		builder.send();

		verify(mailer).sendInternal(entry("sender@email.com"), null, email("recipient@email.com"), empty(), empty(), "Subject line", "Email body", "text/html");
	}

	@Test
	public void shouldNotFailToTheMailBuilderIsNotAMailBuilderImpl() {
		MailBuilder builder = mock(MailBuilder.class);
		when(builder.from()).thenReturn(entry("sender@email.com"));
		when(builder.to()).thenReturn(email("recipient@email.com"));
		when(builder.body()).thenReturn(new StringView("Email body").contentType(null));
		when(builder.subject()).thenReturn("Subject line");
		mailer.send(builder);

		verify(mailer).sendInternal(entry("sender@email.com"), null, email("recipient@email.com"), empty(), empty(), "Subject line", "Email body", "text/html");
	}

	@Test
	public void shouldSaveThenRestoreAttributesOnRequestDuringContentRendering() {
		viewResolverRegistry.addResolver(String.class, new ViewResolver<String>() {
			@Override
			public void resolve(HttpServletRequest req, HttpServletResponse resp, String viewResult) {
				req.setAttribute("updated", 2);
				req.removeAttribute("initial");
				try {
					resp.getWriter().write(viewResult);
				} catch (IOException e) {
					throw new BaseException(e);
				}
				assertThat(BaseMailerTest.this.req.getAttribute("initial"), is(nullValue()));
				assertThat(BaseMailerTest.this.req.getAttribute("updated"), is((Object) 2));
			}
		});

		req.setAttribute("initial", "value");
		req.setAttribute("updated", 1);

		assertThat(req.getAttribute("initial"), is((Object) "value"));
		assertThat(req.getAttribute("updated"), is((Object) 1));

		MailBuilder builder = mailer.mail(req);
		builder.from("sender@email.com");
		builder.to("recipient@email.com");
		builder.body("Email body");
		builder.subject("Subject line");
		builder.send();

		verify(mailer).sendInternal(entry("sender@email.com"), null, email("recipient@email.com"), empty(), empty(), "Subject line", "Email body", "text/html");

		assertThat(req.getAttribute("initial"), is((Object) "value"));
		assertThat(req.getAttribute("updated"), is((Object) 1));

	}

	public static final Map<String, String> email(String email) {
		return Expressive.map(email, null);
	}

	public static final Map<String, String> email(String email, String name) {
		return Expressive.map(email, name);
	}

	public static final Map<String, String> empty() {
		return Expressive.map();
	}

	@SuppressWarnings("unchecked")
	public static final Map.Entry<String, String> anyEntry() {
		return Mockito.any(Map.Entry.class);
	}

	public static final Map<String, String> anyMap() {
		return Mockito.anyMapOf(String.class, String.class);
	}
}
