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

public interface MailBuilder {
	public void send() throws MailException;

	public <T> T body();

	public <T> MailBuilder body(T view) throws MailException;

	public String subject();

	public MailBuilder subject(String subject);

	public Map.Entry<String, String> from();

	public MailBuilder from(String emailAddress);

	public MailBuilder from(String emailAddress, String name);

	public Map<String, String> to();

	public MailBuilder to(String emailAddress);

	public MailBuilder to(String emailAddress, String name);

	public MailBuilder to(Map<String, String> to);

	public Map<String, String> cc();

	public MailBuilder cc(String emailAddress);

	public MailBuilder cc(String emailAddress, String name);

	public MailBuilder cc(Map<String, String> cc);

	public Map<String, String> bcc();

	public MailBuilder bcc(String emailAddress);

	public MailBuilder bcc(String emailAddress, String name);

	public MailBuilder bcc(Map<String, String> bcc);

	public Map.Entry<String, String> replyTo();

	public MailBuilder replyTo(String email);

	public MailBuilder replyTo(String email, String name);

}
