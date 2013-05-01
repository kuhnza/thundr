---
id: properties
title: Properties

---

#### Properties

After instantiation, the injection context will attempt to set any javabean properties it can. It does this using standard setter style methods.

As with instantiation, named instances and named types take preference over just instances and types.

The injection context also supports a subset of JSR 330. This allows you to put the `@Inject` annotation directly on a member variable, rather than using a javabean property.
Note that the InjectionContext does not currently support the full JSR 330 specification, just the basic @Inject annotation.

Where thundr interprets constructor arguments as *mandatory*, properties specified using setters or `@Inject`are considered *optional*. That is, no `InjectionException` will be thrown if the injection configuration cannot satisfy the desired parameters.

In general, this division between mandatory and optional properties should give you guidance as to whether a given property should be a constructor argument, or a simple property on your types.

The following is an example of the three basic ways of injecting required dependencies:

```java
...
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
...
```
