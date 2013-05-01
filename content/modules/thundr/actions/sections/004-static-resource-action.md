---
id: staticResourceAction
title: StaticResourceAction
template: sections/accordion.hbs

---

#### StaticResourceAction

This type of action results in a resource being served directly. This is useful when serving javascript, css, text files or other resources which don't require any processing by your application.

The routes below result in the favicon and all files under the folder 'static' being server as static resources. 

```java
...
"/favicon.ico": "static",
"/static/**": "static",
...
```
