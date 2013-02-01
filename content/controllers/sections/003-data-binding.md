---
id: dataBinding

---

#### Data binding

When a controller method is invoked, data from the request is bound and passed in using the method parameters.

thundr binds data using the names of the variables and automatically converts to the target types.

Data is bound from the following locations:

-	**Request parameters** - request parameters include request parameters for GET/DELETE request and form parameters for POST/PUT requests.

	`http://localhost:8080/page?param1=parameter%20value&param2=2` will bind to 

	```java
	public JspView method(String param1, Long param2){ … }
	```

	likewise,

	```html
	<form action="/page" method="post">
		<input type="text" name="param1" value="parameter value"/>
		<input type="text" name="param2" value="2"/>
	</form>
	```

	will bind to

	```java
	public JspView method(String param1, Long param2){ … }
	```

	thundr is capable of binding all basic java types, collection classes and javabeans.

-	**Path variables** - that is named variables as portion of the route. e.g.
	
	```java
	"/{path}/{variables}" : "com.threewks.web.Controller.method"
	```

	will bind to

	```java
	public JspView method(long path, String variables){ … }
	```

- 	**Multipart data** - we can post files for example like this:
	
	```html
	<form action="/page" enctype="multipart/form-data" method="post">
		<input type="text" name="filename" value=""/>
		<input type="file" name="fileData" value=""/>
	</form>
	```

	which would bind to

	```java
	public JspView postFile(String filename, byte[] fileData){ … }
	```

-	**Json** - using [Gson](http://code.google.com/p/google-gson/), json requests will bind data automatically.
	
	For example, if the request posted json like this

	```json
	{
		"id": 123,
		"names": ["first", "last"] 
	}
	```

	and we have a class like this:

	```java
	package com.mycompany;
	public class MyDTO {
		private int id;
		private List<String> names;
		
		public void setId(int id){
			this.id = id;
		}
		public void setNames(List<String> names){
			this.names = names;
		}
		public List<String> getNames(){
			return names;
		}
		public int getId(){
			return id;
		}
	}
	```

	it can automatically bind onto

	```java
	public JspView method(MyDTO dto){ … }
	```

- 	**Servlet classes** - thundr will pass in a `HttpServletRequest`, `HttpServletResponse` and an `HttpSession` object.
	e.g. 

	```java
	public JspView view(HttpServletRequest req, HttpServletResponse resp){ … }
	```

- 	**Request headers** - for example we could get the referer using the following: 
	
	```java
	public JspView method(String Referer){ … }
	```

When data binding is used, you can generally mix any types of bindings, for example using path variables, request parameters and the `HttpServletRequest`. This gives you the flexibility to generally consume most types of http integration.

> Remember, if you can't automatically bind the data in your request, you can always just bind the HttpServletRequest and HttpServletResponse.
> This is equivalent to writing a servlet, so you'll have to do the heavy lifting yourself, but it means that as long as a servlet could handle the input, your thundr controller can as well.