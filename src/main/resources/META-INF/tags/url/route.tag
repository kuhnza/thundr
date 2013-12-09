<%--

    This file is a component of thundr, a software library from 3wks.
    Read more: http://www.3wks.com.au/thundr
    Copyright (C) 2013 3wks, <thundr@3wks.com.au>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@tag import="java.util.Enumeration"%>
<%@tag import="java.util.Map"%>
<%@tag import="com.threewks.thundr.route.Routes"%>
<%@tag import="com.threewks.thundr.route.Route"%>
<%@ tag dynamic-attributes="pathVars" trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	Map<String, Object> pathVars =  (Map<String, Object>)getJspContext().getAttribute("pathVars", PageContext.PAGE_SCOPE);
	Routes routes = (Routes)request.getAttribute("routes");
	Route route = routes.getRoute(name);
	String reverse = route.getReverseRoute(pathVars);
	out.print(reverse);
%>