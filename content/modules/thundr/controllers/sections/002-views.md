---
id: views
title: Views

---

#### Views

In the above example, we see the `JspView` being returned from our controller. This resulted in a Jsp being processed and sent back to the user.
thundr supports different views, which control the content and http response returned to the user.

- JspView - renders a jsp - [more](modules/thundr/views.html#jspView)
- RedirectView - redirects the client to a new url - [more](modules/thundr/views.html#redirectView)
- JsonView - uses the [Gson library](http://code.google.com/p/google-gson/) to return json to the client - [more](modules/thundr/views.html#jsonView)
- StringView - returns a string to the client - [more](modules/thundr/views.html#stringView)
- HttpStatusException - when thrown, this exception results in an error code being sent to the request client - [more](modules/thundr/views.html#httpStatusException)
- StringView - returns a string to the client - [more](modules/thundr/views.html#stringView)