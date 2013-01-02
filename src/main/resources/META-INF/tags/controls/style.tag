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
<%@ tag body-content="empty" dynamic-attributes="dynAttrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="src" required="true" description="The source file for the style to reference. If a relative file is given, it is assumed to be from the /static/styles/ web app location." %>
<%@ attribute name="media" required="false" %>
<%@ attribute name="suppressVersion" required="false" type="java.lang.Boolean" description="If set to true, scripts will not append the application version as a request parameter. This is useful to keep resources inline with application deployments" %>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${not fn:startsWith(src, '/') and not fn:startsWith(src, 'http')}">
<c:set var="src" value="/static/styles/${src}"/>
</c:if>
<c:if test="${not suppressVersion}">
	<c:choose>
	<c:when test="${fn:contains(src, '?')}">
		<c:set var="src" value="${src}&${applicationVersion}"/>
	</c:when>
	<c:otherwise>
		<c:set var="src" value="${src}?${applicationVersion}"/>
	</c:otherwise>
	</c:choose>
</c:if>
<c:set var="type" value=""/>
<c:if test="${fn:contains(src, '.less')}">
<c:set var="type" value="/less"/>
</c:if>
<c:if test="${empty media}">
<c:set var="media" value="screen, projection"/>
</c:if>
<link href="${src}" rel="stylesheet${type}" type="text/css" media="${media}" <c:forEach items="${dynAttrs}" var="dynAttr">${dynAttr.key}="${dynAttr.value}"</c:forEach>></link>