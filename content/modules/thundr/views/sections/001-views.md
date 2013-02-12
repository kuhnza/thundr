---
id: views
title: Views

---

#### Views

[Controller](modules/thundr/controllers.html) methods can return a `View` object, which controls what content is returned to the request client.

thundr handles views by passing them to registered a `ViewResolver`. You can read more about registering custom view and view resolvers [here](#viewResolver).

Below are the views that thundr supports out of the box, you can also inspect the ViewResolver hierarchy to see all implementations.
