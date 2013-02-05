---
id: methodAction
title: MethodAction

---

#### MethodAction

This type of action results in a method being invoked on a class.
Classes invoked like this are referred to as *controllers*, however this is a semantic distinction - any class can be used as a controller.
You can read more about controllers [here](controllers.html).

For example, the route

```java	
…
"/route/{variable}": "com.threewks.thundr.Controller.actionMethod"
…
```

will invoke the method

```java
package com.threewks.thundr;
public class Controller {
	public StringView actionMethod(String variable){
		doSomething();
		return new StringView("Ok!");
	}
…

```