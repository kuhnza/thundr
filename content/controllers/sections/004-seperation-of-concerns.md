---
id: separationOfConcerns

---

#### Separation of concerns


The job of a controller is to service a request by performing any behaviour and marshalling response data into the form that is required by the resulting view. 

In order to write maintainable, testable applications you should be sure to make sure your application leverages the concept of ['separation of concerns'](http://en.wikipedia.org/wiki/Separation_of_concerns).
In this case, what we mean by that is that while the controller is designed for invoking behaviour, in general it should not actually contain behavioural logic as much as possible.

To help you achieve this aim, controllers can have other instances passed into them at creation time.
This allows you to inject dependencies, such as business process, service and repository code.

In the example below, two services are injected using the constructor at creation time, and then invoked on page requests:

Controller - Controller.java

```java
…
public class Controller {
	private DocumentService documentService;
	private CounterRepository counterRepository;
	
	public Controller(DocumentService documentService, CounterRepository counterRepository){
		this.documentService = documentService;
		this.counterRepository = counterRepository;
	}
	public JspView home() {
		counterRepository.incrementHomeViews();

		List<Document> recentlyViewed = documentService.getRecentlyViewedDocuments()
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("recentlyViewed", recentlyViewed);
		return new JspView("home.jsp", model);
	}
}
```

