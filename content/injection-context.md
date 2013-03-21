---
layout: layout.hbs
template: page.hbs
id: injectionContext
nav_position: 6

title: dependancy injection

---

## Overview

thundr utilises the [dependency injection](http://en.wikipedia.org/wiki/Dependency_injection) pattern to help configure your code at runtime. In particular, any classes you wish to use as [controllers](controllers.html) are created by thundr using its dependency injection framework.
If your controller requires dependencies, they need to be configured so that thundr can provide them.

Unlike many frameworks, dependencies are configured using basic java in thundr. It does this by enforcing a specified entry point for configuring your application, which is your InjectionConfiguration class. 

Your application uses an InjectionConfiguration to make available all the instances and classes you wish to inject into your controllers and each-other. It is up to you how much you wish to rely on this, but in general utilising dependency injection increases the testability of controllers and other code resources, and reduces code coupling. 

Your injection configuration needs to implement `com.threewks.thundr.injection.InjectionConfiguration`. It also has to have a specific name, which is **X**InjectionConfiguration where **X** is the name of the package it resides in. This requirement means that thundr doesn't need to perform any classpath scanning to locate configuration resources.

For example, if your injection configuration is in the package `com.mycompany.myapp`, your configuration must be the java class `com.mycompany.myapp.MyappInjectionConfiguration`. In this case, you *module* will be com.mycompany.myapp.

com.mycompany.myapp.MyappInjectionConfiguration

```java
package com.mycompany.myapp;

import com.threewks.thundr.injection.BaseInjectionConfiguration;
import com.threewks.thundr.injection.InjectionConfiguration;
import com.threewks.thundr.injection.UpdatableInjectionContext;
import com.threewks.thundr.profiler.BasicProfiler;
import com.threewks.thundr.profiler.Profiler;

public class MyappInjectionConfiguration implements InjectionConfiguration {

	@Override
	public void configure(UpdatableInjectionContext injectionContext) {
		injectionContext.inject(MyServiceImpl.class).as(MyServiceInterface.class);
		injectionContext.inject(MyRepositoryImpl.class).as(MyRepositoryInterface.class);
	}
}
```