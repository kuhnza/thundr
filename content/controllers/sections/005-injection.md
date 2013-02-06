---
id: injection
title: Injection

---

#### Controller Instantiation

Controller classes are instantiated by the [InjectionContext](injection-context.html#injectionContext). As such each instance can be shared between multiple, concurrent requests, so implementations should be treated as stateless.
Also, there is no guarantee that controllers are singletons and implementations should also take this into account.

When a controller is instantiated, the injection context will attempt to use the constructor with the most arguments that it can satisfy.
The injection context will also invoke javabean setters for any objects it can satisfy.
Non-javabean properties can also be set by using the JSR 330 annotation `@Inject` on the property. Note that the InjectionContext does not currently support the full JSR 330 specification, just the basic @Inject annotation.

You can read more [here](injection-context.html) about the InjectionContext, dependency injection in thundr and how to write controller and service classes the 'right' way. 

The below example demonstrates the different ways dependencies can be injected into controllers.

Controller - Controller.java

```java
…
public class Controller {
	private DocumentService documentService;
	private CounterRepository counterRepository;
	@Inject
	private CategoryService categoryService;
	
	public Controller(DocumentService documentService){
		this.documentService = documentService;
	}
	public void setCounterRepository(CounterRepository counterRepository){
		this.counterRepository = counterRepository;
	}
…
```