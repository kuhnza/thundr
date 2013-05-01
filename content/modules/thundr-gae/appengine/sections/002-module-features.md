---
id: moduleFeatures
title: Module Features

---

#### Module Features

-	##### SearchService
	
	The module provides an abstraction on top of the native [AppEngine search service](https://developers.google.com/appengine/docs/java/search/). This abstraction allows you to interact with the search service using your
	java object model (i.e. pojos and javabeans), rather than the low level api. The search service is implemented by the *class com.threewks.thundr.search.google.GoogleSearchService*.
	
	```java
	...
	private SearchService searchService;

	public MyController(SearchService searchService){
		this.searchService = searchService;
	}
	
	public DomainModel search(String query, long time){
	SearchRequest<DomainModel> query = searchService.search(DomainModel.class);
		// to perform a general query across all fields
		query = query.query("search terms");
		
		// to perform a query on a specific field
		query = query.field("timestamp").greaterThan(time);
	}
	...
	```
	
	As you can see from this simple example, the SearchService provides a fluent api for querying specific fields.
	It also provides facilities for limits, orders and offsets.	

-	##### HttpService
	
	The module provides an implementation of the HttpService defined in the thundr-http module <span class="label label-warning">Undocumented</span>. This is required because of the need to use the [UrlFetchService](https://developers.google.com/appengine/docs/java/urlfetch/). You can then inject it into your controllers and services like this:

	```java
	...
	private HttpService httpService;

	public MyController(HttpService httpService){
		this.httpService = httpService;
	}
	...
	```

-	##### CounterRepository
	
	Atomic incrementing counters can be a little tricky in distributed environment. *thundr-gae* provides an implementation for sharding counters based on the discussion [here](https://developers.google.com/appengine/articles/sharding_counters).
	
	You can use this in your project by adding it to your injection configuration like this:

	```java
	...
	MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
	DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	CounterRepository counterRepository = new DatastoreCounterRepository(cacheService, datastoreService);
	injectionContext.inject(counterRepository).as(CounterRepository.class);
	...
	```

	You can later use this in code like this:

	```java
	long currentCount = counterRepository.incrementCount("CounterType", entityId);
	```