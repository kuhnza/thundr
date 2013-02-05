---
id: httpStatusException
title: HttpStatusException

---

#### HttpStatusException


`HttpStatusException` is a RuntimeException which takes a status code as a constructor parameter.

When thrown, the specified status code is returned to the client as an http error, along with the specified message.

```java
…
throw new HttpStatusException(HttpServletResponse.SC_BAD_REQUEST, "Bad request at %d", System.currentTimeMillis());
…
```
					
The servlet engine will process the response code as normal, so if your web.xml maps a jsp, then this is the content returned to the client. e.g.
				
```xml
…
<error-page>
	<error-code>404</error-code>
	<location>/WEB-INF/jsp/errors/404.jsp</location>
</error-page>
…
```
