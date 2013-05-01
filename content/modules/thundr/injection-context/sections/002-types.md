---
id: injectionTypes
title: Injection by type

---

#### Injection by type


For the injection context to be able to create and provide instances, it needs to know about the types. This is achieved by registering them. Types can only be registered through the interface `UpdatableInjectionContext`. This interface is provided to `InjectionConfiguration` implementations.

You can register either an instance, or more commonly, the concrete type you wish instantiated. Registration requires the interface type that the desired type or instance is returned for to be specified.

For example:

```java
...
public void configure(UpdatableInjectionContext injectionContext) {
	injectionContext.inject(MyServiceImpl.class).as(MyServiceInterface.class);
	MyRepositoryImpl repository = new MyRepositoryImpl();
	injectionContext.inject(repository).as(MyRepositoryInterface.class);
}
...
```

In the above example, we register the type `MyServiceImpl` to be returned when a `MyServiceInterface` is requested, and a specific instance of `MyRepositoryImpl` to be returned when a `MyRepositoryInterface` is requested.
After this, the following is possible:

```java
...
	MyServiceInterface myService = injectionContext.get(MyServiceInterface.class);
	MyRepositoryInterface myRepository = injectionContext.get(MyRepositoryInterface.class);
...
```