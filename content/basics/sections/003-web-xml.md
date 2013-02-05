---
id: webXml
title: web.xml
starts_new_category: true

---

#### `webXml`

The minimum web.xml required for a thundr app is to include the thundr servlet.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app
	xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	version="2.5">
	<servlet>
		<servlet-name>servlet</servlet-name>
		<servlet-class>com.threewks.thundr.WebFrameworkServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>servlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
</web-app>
```

Beyond this, you can use any servlets, servlet filters or other web.xml features you want. Remember that thundr is capable of invoking a servlet as a controller if you so wish. 