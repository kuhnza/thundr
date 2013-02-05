---
id: viewResolver
title: ViewResolver

---

#### `ViewResolver` <span class="label label-info">Advanced</span>

The `ViewResolverRegistry` returns a `ViewResolver` based on the class type of the view returned from a controller method. The view resolver is then given the view instance, the servlet request and servlet response and is expected to generate some output to the servlet response.

The view resolver registry is available in the `InjectionContext` during startup. An instance of a `ViewResolver` can be registered at this point against **any** class type. That is, while most views do implement the `View` interface, this is for convenience and not a requirement.