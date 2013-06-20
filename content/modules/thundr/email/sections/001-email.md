---
id: email
title: Email

---

#### Email

thundr has framework support for sending of emails. To send an email, you use the fluent api returned from the `Mailer.mail(HttpServletRequest req)` method.
		 
The `MailBuilder` returned from this method has a variety of email specific properties you can set. In addition, you specify the body of the email using the standard `View` framework of thundr. That is, you can provide the body of the email using anything you could render a [view](/modules/thundr/views.html) with. 
