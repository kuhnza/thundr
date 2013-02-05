---
id: redirectView
title: Redirect View

---

#### RedirectView


`RedirectView` results in a temporary redirect (302) being sent back to the client with the specified location. This will send a browser to the new url.

```java
…
return new RedirectView("http://www.google.com.au/search?q=thundr");
…
```