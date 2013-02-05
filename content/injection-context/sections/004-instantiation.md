---
id: instantiation
title: Instantiation

---

#### Instantiation

When an instance is requested from the InjectionContext it will return an existing instance if possible, and if not, it will instantiate a new one.

To create a new instance, the injection context will invoke the constructor with the most number of arguments it can satisfy. If no constructor can be satisfied, an `InjectionException` will be thrown.
A constructor can only be satisfied if the injection context can find at least a matching type for each argument.

As well as matching type, the injection context will attempt to match the name. If a named type or instance was registered with the injection context, and the parameter name of the argument match it will be supplied to constructor over a type or instance registered without a name.

For example:

```java
… 
public void configure(UpdatableInjectionContext injectionContext) {
	injectionContext.inject(MyServiceImpl.class).named("myService").as(MyServiceInterface.class);
	injectionContext.inject(new MyServiceImpl("debugMode")).named("debugService").as(MyServiceInterface.class);
	injectionContext.inject(MyProcess.class).as(MyProcess.class);

	MyProcess myProcess = injectionContext.get(MyProcess.class);
}
…

public class MyProcess {
	public MyProcess(MyServiceInterface myService, MyServiceInterface debugService){
	…
	}
}
…
```

In the above example, we inject two different `MyServiceInterface`s into our context, and when we get an instance of `MyProcess`, the constructor is invoked with each parameter being satisfied by the corresponding named type.