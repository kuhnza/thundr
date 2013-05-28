---
id: moduleFeatures
title: Module Features

---

#### Module Features

-	##### Mailgun.sendEmail
	
	Email can be sent through the mailgun [messages](http://documentation.mailgun.com/api-sending.html#api-sending-messages) service using the sendEmail method.
	
	```java
	...
		MessageSend sendResult = mailgun.sendEmail("sender@domain.com", "to@domain.com", null, "Subject", "<html><body>Content</body></html>");
	...
	```

-	##### Mailgun.log

	Logs of recent email sends can be retrieved using the mailgun [log](http://documentation.mailgun.com/user_manual.html#logs) service using the log method.
	It returns a Log object, which contains a count and zero or more LogItem objects
	
	```java
	...
		int offset = 0;
		int limit = 100;
		Log log = mailgun.log(offset, limit);
	...
	```