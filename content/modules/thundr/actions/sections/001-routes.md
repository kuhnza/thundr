---
id: routes
title: Routes

---

## Routes

Incoming web requests are handled by thundr using *routes*.

A route is a url wildcard pattern which maps to an *action*. Each route is of a http request type - *GET*, *PUT*, *POST* or *DELETE*. 

By default routes are loaded from the resource file `routes.json`. Within this file, routes are defined as a json structure.

```json
{
	"/" 		: "au.com.threewks.thundrweb.Controller.home",
	"/form/" 	: {
		"GET" 	: "au.com.threewks.thundrweb.Controller.getForm",
		"POST" 	: "au.com.threewks.thundrweb.Controller.postForm"
	},
	"/view/{id}"	: "au.com.threewks.thundrweb.Controller.view"
}
```


Routes can map to different types of actions. The most common action is `MethodAction`, which will result in a method being invoked on a [controller](modules/thundr/controllers.html). 

In the above example, requests to the base url '/' will be processed by the *home* method defined in the *Controller* class.
GET Requests on '/form/' will be processed by *getForm()* and POSTs will be processed by *postForm()*.
Finally, requests matching the path `/view/*` will be processed by the method with the name *view*, with the addition that the value matching the wildcard can be passed as a parameter with the name 'id', permitting *view(String id)*.			