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
<%@ attribute name="id"%>
<%@ attribute name="checked"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="cssClass"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty id}"><c:set var="id" value="id='${id}'"/></c:if>
<c:if test="${not empty cssClass}"><c:set var="cssClass" value="class='${cssClass}'"/></c:if>
<c:if test="${not empty value}"><c:set var="value" value="value='${value}'"/></c:if>
<input ${id} ${cssClass} name="${name}" <c:if test="${checked}">checked="checked"</c:if> <c:forEach items="${dynAttrs}" var="dynAttr">${dynAttr.key}="${dynAttr.value}"</c:forEach> type="checkbox" />
