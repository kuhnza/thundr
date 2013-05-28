---
id: settingUp
title: Setting Up

---



#### Setting Up

Including the *thundr-mailgun* jar in your project will enable you to instantiate a MailgunImpl, which provides access to Mailgun APIs through the Mailgun interface.
Mailgun depends on an implementation of the thundr-http module. 

pom.xml
```xml
...
<dependency>
	<groupId>com.threewks.thundr</groupId>
	<artifactId>thundr-mailgun</artifactId>
	<version>0.9.9</version>
	<scope>compile</scope>
</dependency>
<!-- thundr-http-ning or thundr-gae -->
<dependency>
	<groupId>com.threewks.thundr</groupId>
	<artifactId>thundr-http-ning</artifactId>
	<version>0.9.9</version>
	<scope>compile</scope>
</dependency>
...
```

```java
...
HttpService httpService;
Mailgun mailgun = new MailgunImpl(httpService, "mydomain.mailgun.org", "key_myApiKey");
mailgun.sendEmail("sender@domain.com", "to@domain.com", null, "Subject", "<html><body>Content</body></html>");
...

```
