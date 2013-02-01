---
id: controllers

---

We refer to the classes invoked when using the typical [route](actions.html#routes) as *controllers*.

When a http client makes a request, the appropriate method inside a controller is invoked. The method can perform processing and gather data for the response, then return a `View` object. The view controls the content and http response codes that are returned to the client.  

Controller classes are ordinary classes, they don't require implementing any interfaces or annotations to function. Having one or more routes directing to a method inside a class is enough for thundr to [create](#injection) and invoke your controller class when a request is made.


#### Basic Example

In the very basic example below, the controller renders the home.jsp for its view, to which it provides a simple model with the message 'Hello World!'

Controller - Controller.java

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