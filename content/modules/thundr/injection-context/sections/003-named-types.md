---
id: namedTypes
title: Injection by named type

---

#### Injection by named type

As well as specifying types, you can also specified *named types*.Named types are types or instances registered as above, but also with specific names. For example: 

```java
...
public void configure(UpdatableInjectionContext injectionContext) {
	injectionContext.inject(MyServiceImpl.class).named("myService").as(MyServiceInterface.class);
	injectionContext.inject(new MyServiceImpl("debugMode")).named("alternativeService").as(MyServiceInterface.class);
}
...
```

In the above example, we register two different different `MyServiceInterface`s, with two different names.
When an instance is requested from the injection context, a different instance is returned depending on the name specified.

```java
...
	MyServiceInterface myService1 = injectionContext.get(MyServiceInterface.class, "myService");
	MyServiceInterface myService2 = injectionContext.get(MyServiceInterface.class, "alternativeService");
...
```