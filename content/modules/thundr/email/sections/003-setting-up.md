---
id: settingUp
title: Setting Up

---

#### Setting Up

To set up the Mailer, you just need to make it available in the `InjectionContext` in your [InjectionConfiguration](/modules/thundr/basics.html#injectionConfiguration).

```java
...
injectionContext.inject(Mailer.class).as(Mailer.class);
...

```
