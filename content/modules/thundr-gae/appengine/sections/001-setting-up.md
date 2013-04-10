---
id: settingUp
title: Setting Up

---



#### Setting Up

Beyond [normal thundr setup](modules/thundr/basics.html#applicationConfiguration) (i.e. `web.xml`, `application.properties`, `modules.properties` and an `InjectionConfiguration`), you will need to pull in the *thundr-gae* module and configure it.

This is accomplished by adding the *thundr-gae* library to your project (or adding the dependency to your maven or similar config). You then need to load the *thundr-gae* module by adding it to your `modules.properties` file.

pom.xml
```xml
…
<dependency>
	<groupId>com.threewks.thundr</groupId>
	<artifactId>thundr-gae</artifactId>
	<version>0.9.7</version>
	<scope>compile</scope>
</dependency>
…
```

modules.properties

```java
com.mycompany.myapp=
com.threewks.thundr.gae=
```

> Remember order is important in modules.properties - include the *thundr-gae* module **after** you application.