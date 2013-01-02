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

import static com.atomicleopard.expressive.Expressive.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.Message.RecipientType;
import javax.servlet.http.HttpServletRequest;

import com.atomicleopard.expressive.collection.Triplets;
import com.threewks.thundr.view.ViewResolver;

public class MailBuilderImpl implements MailBuilder {
	private Mailer mail;
	private HttpServletRequest request;
	private String content;
	private String subject;
	private Map<String, String> from = new HashMap<String, String>();
	private Map<String, String> replyTo = new HashMap<String, String>();
	private Triplets<RecipientType, String, String> recipients = new Triplets<RecipientType, String, String>();

	public MailBuilderImpl(Mailer mail, HttpServletRequest request) {
		this.mail = mail;
		from.put("", null);
		this.request = request;
	}

	@Override
	public <T> MailBuilderImpl body(T view) {
		/*
		 * Wrapping the request is highly sensitive to the app server implementation.
		 * For example, while the Servlet include interface specifies we can pass in a {@link ServletRequestWrapper},
		 * Jetty is having none of it. To avoid ramifications across different application servers, we just reuse the
		 * originating request. To help avoid issues, we restore all attributes after the response is rendered.
		 */
		HttpServletRequest req = request;
		Map<String, Object> attributes = getAttributes(req); // save the current set of request attributes
		try {
			ViewResolver<T> viewResolver = mail.viewResolverRegistry().findViewResolver(view);
			MailHttpServletResponse resp = new MailHttpServletResponse();
			viewResolver.resolve(req, resp, view);
			content = resp.getResponseContent();
		} catch (Exception e) {
			throw new MailException(e, "Failed to render email body: %s", e.getMessage());
		} finally {
			setAttributes(req, attributes); // reapply the attributes, removing any new ones
		}
		return this;
	}

	@Override
	public void send() {
		mail.send(this);
	}

	@Override
	public MailBuilder subject(String subject) {
		this.subject = subject;
		return this;
	}

	@Override
	public MailBuilder from(String emailAddress) {
		this.from.clear();
		this.from.put(emailAddress, null);
		return this;
	}

	@Override
	public MailBuilder from(String emailAddress, String name) {
		this.from.clear();
		this.from.put(emailAddress, name);
		return this;
	}

	@Override
	public MailBuilder to(String emailAddress) {
		recipients.put(RecipientType.TO, emailAddress, null);
		return this;
	}

	@Override
	public MailBuilder to(String emailAddress, String name) {
		recipients.put(RecipientType.TO, emailAddress, name);
		return this;
	}

	@Override
	public MailBuilder to(Map<String, String> to) {
		for (Map.Entry<String, String> entry : to.entrySet()) {
			recipients.put(RecipientType.TO, entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public MailBuilder cc(String emailAddress, String name) {
		recipients.put(RecipientType.CC, emailAddress, name);
		return this;
	}

	@Override
	public MailBuilder cc(Map<String, String> cc) {
		for (Map.Entry<String, String> entry : cc.entrySet()) {
			recipients.put(RecipientType.CC, entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public MailBuilder bcc(String emailAddress, String name) {
		recipients.put(RecipientType.BCC, emailAddress, name);
		return this;
	}

	@Override
	public MailBuilder bcc(Map<String, String> bcc) {
		for (Map.Entry<String, String> entry : bcc.entrySet()) {
			recipients.put(RecipientType.BCC, entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public MailBuilder replyTo(String email, String name) {
		this.replyTo.clear();
		this.replyTo.put(email, name);
		return this;
	}

	Map.Entry<String, String> from() {
		return from.entrySet().iterator().next();
	}

	Triplets<RecipientType, String, String> recipients() {
		return recipients;
	}

	String subject() {
		return subject;
	}

	String content() {
		return content;
	}

	Entry<String, String> replyTo() {
		return replyTo.isEmpty() ? null : replyTo.entrySet().iterator().next();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getAttributes(HttpServletRequest request) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		for (String name : iterable((Enumeration<String>) request.getAttributeNames())) {
			attributes.put(name, request.getAttribute(name));
		}
		return attributes;
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(HttpServletRequest request, Map<String, Object> attributes) {
		List<String> allNames = list(iterable(request.getAttributeNames())).addItems(attributes.keySet());
		for (String name : allNames) {
			request.setAttribute(name, attributes.get(name));
		}
	}
}
