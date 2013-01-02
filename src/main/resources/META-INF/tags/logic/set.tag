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
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="var" required="false" description="Name of the exported scoped variable to hold the value specified in the action. The type of the scoped variable is whatever type the value expression evaluates to." %>
<%@ attribute name="value" required="false" description="Expression to be evaluated." %>
<%@ attribute name="scope" required="false" description="Scope for var." %>
<%@ attribute name="condition" required="false" description="If this condition is not met, the value will not be set. If not provided, defaults to true" %>
<%@ attribute name="falseValue" required="false" description="If the condition is not met, this is what the value will be set to. If not supplied, no change is made" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags" %>
<t:if condition="${empty condition or condition}">
	<c:set var="${var}" value="${value}" scope="${scope}"/>
</t:if>
<t:elseif condition="${not empty falseValue}">
	<c:set var="${var}" value="${falseValue}" scope="${scope}"/>
</t:elseif>