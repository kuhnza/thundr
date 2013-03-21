---
id: actionResolver
title: ActionResolver
is_advanced_topic: true
starts_new_category: true

---

#### `ActionResolver` <span class="label label-info">Advanced</span>


Each type of action is handled by an `ActionResolver`. ActionResolvers process an Action when a route is requested. You can create and configure custom Action and ActionResolvers to handle routes, although in general this is not necessary. 

An action resolver has two responsibilities:

- creating an `Action` for a route if it can process that route, and
- resolving an action it created when a route is requested by an http client

Action resolution usually involves binding data based on route configuration and request parameters, and serving up response content and http response codes. This is done using the standard Java Servlet interfaces, `HttpServletRequest` and `HttpServletResponse`.

If you wish to register a custom Action and ActionResolver, you can invoke the method

```java
Routes.addActionResolver(Class<A> actionType, ActionResolver<A> actionResolver);
```

This needs to be done before the routes are loaded. The `Routes` object is available in the `InjectionConfiguration` during startup.
