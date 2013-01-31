---
layout: layout.hbs
template: page.hbs
title: basics
id: basics

---

## Overview

thundr, like most other web frameworks, uses the *Model-View-Controller*, or [MVC](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller), pattern for separation of concerns between data processing and view presentation.

In this pattern, the model and the view and the relationship between them is mediated by a controller.

In thundr, this control is enacted by the method invoked inside your controller class. [more](controllers.html)
The view is represented by a `View` object, which is the return type of the controller method. [more](views.html)
The model is provided to the view by the controller method, but the requirements of this depend on the type of view.