package com.atomicleopard.webFramework.mail;

import java.util.Map;

public interface MailBuilder {
	public void send() throws MailException;

	public <T> MailBuilder body(T view) throws MailException;

	public MailBuilder subject(String subject);

	public MailBuilder from(String emailAddress);

	public MailBuilder from(String emailAddress, String name);

	public MailBuilder to(String emailAddress);

	public MailBuilder to(String emailAddress, String name);

	public MailBuilder to(Map<String, String> to);

	public MailBuilder cc(String emailAddress, String name);

	public MailBuilder cc(Map<String, String> cc);

	public MailBuilder bcc(String emailAddress, String name);

	public MailBuilder bcc(Map<String, String> bcc);

	public MailBuilder replyTo(String email, String name);
}
