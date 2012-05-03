<%@ tag body-content="empty" dynamic-attributes="dynAttrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="src" required="true" description="The source file for the script to reference. If a relative file is given, it is assumed to be from the /static/javascript/ web app location." %>
<%@ attribute name="suppressVersion" required="false" type="java.lang.Boolean" description="If set to true, scripts will not append the application version as a request parameter. This is useful to keep resources inline with application deployments" %>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${not fn:startsWith(src, '/') and not fn:startsWith(src, 'http')}">
<c:set var="src" value="/static/javascript/${src}"/>
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

<c:url var="href" value="${src}" />
<script src="${href}" type="text/javascript" <c:forEach items="${dynAttrs}" var="dynAttr">${dynAttr.key}="${dynAttr.value}"</c:forEach>></script>