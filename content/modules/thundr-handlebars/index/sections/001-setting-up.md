---
id: settingUp
title: Setting Up

---



#### Setting Up

Including the thundr-handlebars module will automatically register the HandlebarsViewResovler.

This is accomplished by adding the *thundr-handlebars* library to your project (or adding the dependency to your maven or similar config). You then need to load the *thundr-handlebars* module by adding it to your `modules.properties` file.

pom.xml
```xml
...
<dependency>
	<groupId>com.threewks.thundr</groupId>
	<artifactId>thundr-handlebars</artifactId>
	<version>0.9.8</version>
	<scope>compile</scope>
</dependency>
...
```

modules.properties

```java
com.mycompany.myapp=
com.threewks.thundr.handlebars=
```
