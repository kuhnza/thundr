package com.atomicleopard.webFramework.mail;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.atomicleopard.webFramework.ViewResolverRegistry;
import com.atomicleopard.webFramework.collection.Pair;
import com.atomicleopard.webFramework.collection.Triplets;

public class Mailer {
	private ViewResolverRegistry viewResolverRegistry;

	public Mailer(ViewResolverRegistry viewResolverRegistry) {
		this.viewResolverRegistry = viewResolverRegistry;
	}

	public MailBuilder mail(HttpServletRequest request) {
		return new MailBuilder(this, request);
	}

	void send(MailBuilder mailBuilder) {
		Map.Entry<String, String> from = mailBuilder.from();
		Triplets<RecipientType, String, String> recipients = mailBuilder.recipients();

		Session emailSession = Session.getDefaultInstance(new Properties());

		try {
			Message message = new MimeMessage(emailSession);
			message.setFrom(emailAddress(from.getKey(), from.getValue()));
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
			return new InternetAddress(address, name);
		} catch (UnsupportedEncodingException e) {
			throw new MailException(e, "Failed to send an email - unable to set a recipient of %s <%s>: %s", address, name, e.getMessage());
		}
	}
}
