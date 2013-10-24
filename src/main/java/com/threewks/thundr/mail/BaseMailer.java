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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.http.ContentType;
import com.threewks.thundr.http.SyntheticHttpServletResponse;
import com.threewks.thundr.view.ViewResolver;
import com.threewks.thundr.view.ViewResolverRegistry;

public abstract class BaseMailer implements Mailer {
	protected ViewResolverRegistry viewResolverRegistry;

	public BaseMailer(ViewResolverRegistry viewResolverRegistry) {
		this.viewResolverRegistry = viewResolverRegistry;
	}

	public MailBuilder mail(HttpServletRequest request) {
		return new MailBuilderImpl(this, request);
	}

	public void send(MailBuilder mailBuilder) {
		String subject = mailBuilder.subject();
		Map.Entry<String, String> from = mailBuilder.from();
		Map.Entry<String, String> replyTo = mailBuilder.replyTo();
		Map<String, String> to = mailBuilder.to();
		Map<String, String> cc = mailBuilder.cc();
		Map<String, String> bcc = mailBuilder.bcc();

		validateFrom(from);
		validateRecipients(to, cc, bcc);

		try {
			SyntheticHttpServletResponse syntheticResponse = renderContent(mailBuilder);
			String content = syntheticResponse.getResponseContent();
			String contentType = syntheticResponse.getContentType();
			contentType = ContentType.cleanContentType(contentType);
			contentType = StringUtils.isBlank(contentType) ? ContentType.TextHtml.value() : contentType;

			sendInternal(from, replyTo, to, cc, bcc, subject, content, contentType);
		} catch (MailException e) {
			throw e;
		} catch (Exception e) {
			throw new MailException(e, "Failed to send an email: %s", e.getMessage());
		}
	}

	protected SyntheticHttpServletResponse renderContent(MailBuilder mailBuilder) {
		/*
		 * Wrapping the request is highly sensitive to the app server implementation.
		 * For example, while the Servlet include interface specifies we can pass in a {@link ServletRequestWrapper},
		 * Jetty is having none of it. To avoid ramifications across different application servers, we just reuse the
		 * originating request. To help avoid issues, we restore all attributes after the response is rendered.
		 */
		Object body = mailBuilder.body();
		SyntheticHttpServletResponse resp = new SyntheticHttpServletResponse();
		MailBuilderImpl mailBuilderImpl = Cast.as(mailBuilder, MailBuilderImpl.class);
		HttpServletRequest req = mailBuilderImpl == null ? null : mailBuilderImpl.request();
		Map<String, Object> attributes = getAttributes(req); // save the current set of request attributes
		try {
			ViewResolver<Object> viewResolver = viewResolverRegistry.findViewResolver(body);
			viewResolver.resolve(req, resp, body);
		} catch (Exception e) {
			throw new MailException(e, "Failed to render email body: %s", e.getMessage());
		} finally {
			setAttributes(req, attributes); // reapply the attributes, removing any new ones
		}
		return resp;
	}

	protected abstract void sendInternal(Entry<String, String> from, Entry<String, String> replyTo, Map<String, String> to, Map<String, String> cc, Map<String, String> bcc, String subject, String content, String contentType);

	protected void validateRecipients(Map<String, String> to, Map<String, String> cc, Map<String, String> bcc) {
		if (Expressive.isEmpty(to) && Expressive.isEmpty(cc) && Expressive.isEmpty(bcc)) {
			throw new MailException("No recipient (to, cc or bcc) has been set for this email");
		}
	}

	protected void validateFrom(Entry<String, String> from) {
		if (from == null || from.getKey() == null) {
			throw new MailException("No sender has been set for this email");
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getAttributes(HttpServletRequest request) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		if (request != null) {
			for (String name : iterable((Enumeration<String>) request.getAttributeNames())) {
				attributes.put(name, request.getAttribute(name));
			}
		}
		return attributes;
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(HttpServletRequest request, Map<String, Object> attributes) {
		if (request != null) {
			List<String> allNames = list(iterable(request.getAttributeNames())).addItems(attributes.keySet());
			for (String name : allNames) {
				request.setAttribute(name, attributes.get(name));
			}
		}
	}
}
