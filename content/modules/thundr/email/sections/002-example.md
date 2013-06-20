---
id: example
title: Example

---

#### Example

```java
public void sendEmail(HttpServletRequest req, String recipientEmail, String recipientName){
	Map<String, Object> model = new HashMap<String, Object>();
	model.put("message", "Hello world!");
	JspView view = new JspView("emails/test.jsp", model);
	
	MailBuilder mail = mailer.mail(req);
	mail.to(recipientEmail, recipientName);
	mail.from("no-reply@mydomain.com", "no-reply");
	mail.replyTo("no-reply@mydomain.com", "no-reply");
	mail.subject("Test email");
	mail.body(view);
	mail.send();
}
```	