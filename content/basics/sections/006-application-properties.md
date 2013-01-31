---
id: applicationProperties

---

#### `application.properties`

thundr automatically loads properties from the `application.properties` file and puts them as named strings into the InjectionContext. This makes them available both in your InjectionConfiguration and also as constructor arguments to injected dependencies.

This file is suitable for specifying configuration values that change per environment. That is, while all the other configuration is generally environmentally agnostic, the application properties will generally not be. 

This allows your built war file to be portable through environments without requiring rebuilding.

```java
threadCount=20
threadCount%dev=2
serverUrl=localhost:8080
serverUrl%production=www.production.com
```

In the above example, all environments will have the properties *threadCount* and *serverUrl* injected into them, but the serverUrl for the environment named 'production' will be different.

> The environment named 'dev' is special, in that it specifically applies to local development, rather than a target deployment environment.