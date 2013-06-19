# thundr

*thundr* is a light-weight Web MVC for java, designed to run in cloud environments.

It is designed to be quick to pick up, and easily but powerfully extended by the inclusion of modules.


## Documentation

You can read more about [thundr here](http://3wks.github.io/thundr/modules/thundr/basics.html).

## Features

### Web MVC
* Powerful data binding in controllers
* Out of the box support for jsp, json, exception handling
* Basic declarative interceptor pattern for controllers

### Lightweight
* Code based Dependency Injection framework 
* Direct control of configuration (in testable code)

### Modular
* Easy to add modules, utilise new libraries and services quickly and easily
* Easy to author modules, get better re-use across your own projects or contribute to the community

### No Magic
* Everything happens in code you can step through
* No classpath scanning at startup
* DI invokes your constructors, not everything needs to be a javabean
* Leaves low level apis available to you for those hard to solve problems


## Getting it

To get started, pull in the latest version of thundr using maven, ivy or another build tool and follow these basic [configuration instructions](http://3wks.github.io/thundr/modules/thundr/basics.html#applicationConfiguration).

Alternatively, you can [clone one of the sample apps](https://github.com/3wks/thundr-sample) and hack on it to get moving straight away.

--------------    
thundr - Copyright (C) 2013 3wks