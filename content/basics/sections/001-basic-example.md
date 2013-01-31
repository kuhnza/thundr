---
id: basicExample

---

#### Basic Example

Routes file - routes.json [more](actions.html)

```json
{
	"/"		: "com.threewks.web.Controller.home"
}
```

Controller - Controller.java [more](controllers.html)

```java
package com.threewks.web;

import java.util.HashMap;
import java.util.Map;
import com.threewks.thundr.view.jsp.JspView;

public class Controller {
	public JspView home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("message", "Hello World!");
		return new JspView("home.jsp", model);
	}
}
```

Jsp - /WEB-INF/jsp/home.jsp [more](views.html#jspView)

```html
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" lang="en-us"/>
	</head>
	<body>
		\${message}
	</body>
</html>
```

With these three files, if you were to visit the url `http://localhost:8080/` you would be served a page with the content 'Hello World!'.

We can see the basics of thundr and the three elements of the MVC interacting here:

- an incoming request is mapped to a controller using a [route](actions.html#routes)
- Our [controller](controllers.html) method populates the **model** map with our message,
- which is then provided to our [view](views.html), a JspView.
- thundr takes care of the rest, serving up our jsp as html, substituting the el variable \${message} with the text 'Hello World!'.