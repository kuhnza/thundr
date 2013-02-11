---
id: redirectAction
title: RedirectAction
template: sections/accordion.hbs

---

#### RedirectAction


This type of action results in a HTTP redirect being sent to the web request client (i.e. the user's browser). This will result in the browser requesting the resource specified in the redirect route.
The redirect is a *temporary redirect*, or 302.

```java
…
"/old": "redirect:/new",
…
```