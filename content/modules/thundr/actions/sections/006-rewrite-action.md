---
id: rewriteAction
title: RewriteAction
template: sections/accordion.hbs

---

#### RewriteAction <span class="label label-warning">Verify</span>

This type of action is used to cause another route to handle the request. It is similar in concept to the rewrite rules in an Apache config, but could also be considered to act as an alias.

In the below example, the rewrite rule will cause the rewritten route to be invoked as though the request client invoked it directly, but unlike a redirect will be served with the url it was requested on.

```java
...
"/requestedPath"	: "rewrite:/rewritten",
"/rewritten"		: "com.threewks.thundr.Controller.method"
...
```