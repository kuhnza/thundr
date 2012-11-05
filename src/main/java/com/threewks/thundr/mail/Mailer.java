package com.threewks.thundr.mail;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.atomicleopard.expressive.collection.Pair;
import com.atomicleopard.expressive.collection.Triplets;
import com.threewks.thundr.view.ViewResolverRegistry;

public class Mailer {
	private ViewResolverRegistry viewResolverRegistry;

	public Mailer(ViewResolverRegistry viewResolverRegistry) {
		this.viewResolverRegistry = viewResolverRegistry;
	}

	public MailBuilder mail(HttpServletRequest request) {
		return new MailBuilderImpl(this, request);
	}

	void send(MailBuilderImpl mailBuilder) {
		Map.Entry<String, String> from = mailBuilder.from();
		Map.Entry<String, String> replyTo = mailBuilder.replyTo();
		Triplets<RecipientType, String, String> recipients = mailBuilder.recipients();

		Session emailSession = Session.getDefaultInstance(new Properties());

		try {
			Message message = new MimeMessage(emailSession);
			message.setFrom(emailAddress(from.getKey(), from.getValue()));
			if (replyTo != null) {
				message.setReplyTo(new Address[] { emailAddress(replyTo.getKey(), replyTo.getValue()) });
			}
			message.setSubject(mailBuilder.subject());
			String content = mailBuilder.content();
			message.setContent(content, "text/html");
			for (Entry<Pair<RecipientType, String>, String> recipient : recipients.entrySet()) {
				message.setRecipient(recipient.getKey().getA(), emailAddress(recipient.getKey().getB(), recipient.getValue()));
			}

			sendMessage(message);
		} catch (MessagingException e) {
			throw new MailException(e, "Failed to send an email: %s", e.getMessage());
		}
	}

	ViewResolverRegistry viewResolverRegistry() {
		return viewResolverRegistry;
	}

	// stubbable for testing
	void sendMessage(Message message) throws MessagingException {
		Transport.send(message);
	}

	private InternetAddress emailAddress(String address, String name) {
		try {
			return StringUtils.isBlank(name) ? new InternetAddress(address) : new InternetAddress(address, name);
		} catch (UnsupportedEncodingException e) {
			throw new MailException(e, "Failed to send an email - unable to set a sender or recipient of %s <%s>: %s", address, name, e.getMessage());
		} catch (AddressException e) {
			throw new MailException(e, "Failed to send an email - unable to set a sender or recipient of %s <%s>: %s", address, name, e.getMessage());
		}
	}
}
