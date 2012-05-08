package com.atomicleopard.webFramework.test.mock.mailer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.atomicleopard.webFramework.collection.Pair;
import com.atomicleopard.webFramework.mail.MailBuilder;

public class MockMailBuilder implements MailBuilder {
	private MockMailer mail;
	private String content;
	private String subject;
	private Map<String, String> from = new HashMap<String, String>();
	private Map<String, String> replyTo = new HashMap<String, String>();
	private Map<String, String> to = new HashMap<String, String>();
	private Map<String, String> cc = new HashMap<String, String>();
	private Map<String, String> bcc = new HashMap<String, String>();
	private Object body;

	public MockMailBuilder(MockMailer mail) {
		this.mail = mail;
	}

	public <T> MailBuilder body(T view) {
		this.body = view;
		return this;
	}

	public void send() {
		mail.send(this);
	}

	public MailBuilder subject(String subject) {
		this.subject = subject;
		return this;
	}

	public MailBuilder from(String emailAddress) {
		this.from.clear();
		this.from.put(emailAddress, null);
		return this;
	}

	public MailBuilder from(String emailAddress, String name) {
		this.from.clear();
		this.from.put(emailAddress, name);
		return this;
	}

	public MailBuilder to(String emailAddress) {
		to.put(emailAddress, null);
		return this;
	}

	public MailBuilder to(String emailAddress, String name) {
		to.put(emailAddress, name);
		return this;
	}

	public MailBuilder to(Map<String, String> to) {
		this.to.putAll(to);
		return this;
	}

	public MailBuilder cc(String emailAddress, String name) {
		this.cc.put(emailAddress, name);
		return this;
	}

	public MailBuilder cc(Map<String, String> cc) {
		this.cc.putAll(cc);
		return this;
	}

	public MailBuilder bcc(String emailAddress, String name) {
		bcc.put(emailAddress, name);
		return this;
	}

	public MailBuilder bcc(Map<String, String> bcc) {
		this.bcc.putAll(bcc);
		return this;
	}

	public MailBuilder replyTo(String email, String name) {
		this.replyTo.clear();
		this.replyTo.put(email, name);
		return this;
	}

	public Pair<String, String> from() {
		Entry<String, String> next = from.entrySet().iterator().next();
		return from.isEmpty() ? null : new Pair<String, String>(next.getKey(), next.getValue());
	}

	public Pair<String, String> replyTo() {
		Entry<String, String> next = replyTo.entrySet().iterator().next();
		return replyTo.isEmpty() ? null : new Pair<String, String>(next.getKey(), next.getValue());
	}

	public String subject() {
		return subject;
	}

	public String content() {
		return content;
	}

	public Map<String, String> bcc() {
		return bcc;
	}

	public Map<String, String> cc() {
		return cc;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBody() {
		return (T) body;
	}

	public Map<String, String> to() {
		return to;
	}

}
