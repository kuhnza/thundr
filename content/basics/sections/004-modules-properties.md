---
id: modulesProperties
title: modules.properties

---

#### `modules.properties`

thundr is extensible, it allows extra features and components to be added to applications. These components are called **modules**.

Modules are configured by adding them to resource *modules.properties*.

This file is a standard java properties file, where only the key values are of interest.
Each entry causes a module to be loaded. The minimum modules file only needs to contain the package for the module which is the application.

for the application defined in com.mycompany.myapp - modules.properties

```java
com.mycompany.myapp=
```

> The order of modules in this file is significant, you should always put your application module at the top.