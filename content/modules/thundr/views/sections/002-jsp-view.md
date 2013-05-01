---
id: jspView
title: JspView
template: sections/accordion.hbs

---

#### JspView
			

`JspView` results in a jsp being processed and served from the Servlet engine. You specify the jsp file location, and optionally provide a data model to back it. Unless the jsp rendering fails, an Html file and a 200 OK will be sent back to the user.  


The jsp file is considered to be relative to the path /WEB-INF/jsp/, unless it is given as an absolute path (i.e. starts with a forward slash, /)

```java
...
Map<String, Object> model = ... 
return new JspView("path/to/my.jsp", model);
...
```