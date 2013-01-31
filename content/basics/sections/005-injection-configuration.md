---
id: injectionConfiguration

---


#### `InjectionConfiguration`

A module is defined as a group of code and an `InjectionConfiguration` file.

You application uses an InjectionConfiguration to make available all the instances and classes you wish to inject into your controllers and each-other

Your injection configuration needs to implement `com.threewks.thundr.injection.InjectionConfiguration`. It also has to have a specific name, which is **X**InjectionConfiguration where **X** is the name of the package it resides in.  

For example, if your injection configuration is in the package `com.mycompany.myapp`, your
configuration must be the java class `com.mycompany.myapp.MyappInjectionConfiguration`. In this case, your *module* will be com.mycompany.myapp.

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

> The naming rules for an InjectionConfiguration are case sensitive! My**a**ppInjectionConfiguration is not the same as My**A**ppInjectionConfiguration.

Rather than implement `InjectionConfiguration`, you can also extend `BaseInjectionConfiguration` for convenience.