---
id: moduleFeatures
title: Module Features

---

#### Module Features

-	##### HandlebarsView and HandlebarsViewResolver
	
	Once you have included the module, you can return a HandlebarsView from any controller method (or provide it for emails) like the following example:
	
	```java
	...
	public HandlebarsView view() {
		Map<String, Object> model = model();
		populateReferenceData(model);
		return new HandlebarsView("view.hbs", model);
	}
	...
	```
