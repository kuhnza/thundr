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
