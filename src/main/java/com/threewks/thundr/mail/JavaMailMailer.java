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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.Expressive;
import com.threewks.thundr.view.ViewResolverRegistry;

public class JavaMailMailer extends BaseMailer {

	public JavaMailMailer(ViewResolverRegistry viewResolverRegistry) {
		super(viewResolverRegistry);
	}

	@Override
	protected void sendInternal(Map.Entry<String, String> from, Map.Entry<String, String> replyTo, Map<String, String> to, Map<String, String> cc, Map<String, String> bcc, String subject,
			String content, String contentType) {
		try {
			Session emailSession = Session.getDefaultInstance(new Properties());

			Message message = new MimeMessage(emailSession);
			message.setFrom(emailAddress(from));
			if (replyTo != null) {
				message.setReplyTo(new Address[] { emailAddress(replyTo) });
			}

			message.setSubject(subject);
			message.setContent(content, contentType);
			addRecipients(to, message, RecipientType.TO);
			addRecipients(cc, message, RecipientType.CC);
			addRecipients(bcc, message, RecipientType.BCC);

			sendMessage(message);
		} catch (MessagingException e) {
			throw new MailException(e, "Failed to send an email: %s", e.getMessage());
		}
	}

	protected void sendMessage(Message message) throws MessagingException {
		Transport.send(message);
	}

	private void addRecipients(Map<String, String> to, Message message, RecipientType recipientType) throws MessagingException {
		if (Expressive.isNotEmpty(to)) {
			for (Entry<String, String> recipient : to.entrySet()) {
				message.setRecipient(recipientType, emailAddress(recipient));
			}
		}
	}

	private InternetAddress emailAddress(Map.Entry<String, String> entry) {
		try {
			return StringUtils.isBlank(entry.getValue()) ? new InternetAddress(entry.getKey()) : new InternetAddress(entry.getKey(), entry.getValue());
		} catch (Exception e) {
			throw new MailException(e, "Failed to send an email - unable to set a sender or recipient of '%s' <%s>: %s", entry.getKey(), entry.getValue(), e.getMessage());
		}
	}
}
