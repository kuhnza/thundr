package com.atomicleopard.webFramework.test.mock.mailer;

import static com.atomicleopard.expressive.Expressive.list;

import javax.servlet.http.HttpServletRequest;

import com.atomicleopard.expressive.EList;
import com.atomicleopard.webFramework.mail.MailBuilder;
import com.atomicleopard.webFramework.mail.Mailer;

public class MockMailer extends Mailer {

	private EList<MockMailBuilder> sent = list();

	public MockMailer() {
		super(null);
	}

	@Override
	public MailBuilder mail(HttpServletRequest request) {
		return new MockMailBuilder(this);
	}

	void send(MockMailBuilder mockMailBuilder) {
		sent.add(mockMailBuilder);
	}

	public EList<MockMailBuilder> getSent() {
		return sent;
	}
}
