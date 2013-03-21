---
id: injectionContext
title: InjectionContext

---

#### `InjectionContext`


Dependency injection is managed by the `InjectionContext`.

The injection context acts like a bucket for instances.
It can both provide existing instances and create new instances of required types. When the injection context creates new instances, it will try to satisfy dependencies using other instances it contains or can create.