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