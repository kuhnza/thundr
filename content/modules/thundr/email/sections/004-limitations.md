---
id: limitations
title: Limitations

---

#### Limitations

thundr mail is build on top of the standard <code>javax.mail</code> api. If your target platform cannot send email using this api, or if it requires additional configuration you will need to configure this yourself.

Currently thundr mail only supports basic html emails. It does not create multipart mails, and does not support embedded content, such as cid references and attachments.