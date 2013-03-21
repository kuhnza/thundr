---
id: moduleFeatures
title: Module Features

---

#### Module Features


-	##### HttpService
	
	The module provides an implementation of the HttpService defined in the thundr-http module <span class="label label-warning">Undocumented</span>. This is required because of the need to use the [UrlFetchService](https://developers.google.com/appengine/docs/java/urlfetch/). You can then inject it into your controllers and services like this:

	```java
	…
	private HttpService httpService;

	public MyController(HttpService httpService){
		this.httpService = httpService;
	}
	…
	```

-	##### CounterRepository
	
	Atomic incrementing counters can be a little tricky in distributed environment. *thundr-gae* provides an implementation for sharding counters based on the discussion [here](https://developers.google.com/appengine/articles/sharding_counters).
	
	You can use this in your project by adding it to your injection configuration like this:

	```java
	…
	MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	CounterRepository counterRepository = new DatastoreCounterRepository(cacheService, datastoreService);
	injectionContext.inject(counterRepository).as(CounterRepository.class);
	…
	```

	You can later use this in code like this:

	```java
	long currentCount = counterRepository.incrementCount("CounterType", entityId);
	```