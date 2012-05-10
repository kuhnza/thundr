<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="var" required="false" description="Name of the exported scoped variable to hold the value specified in the action. The type of the scoped variable is whatever type the value expression evaluates to." %>
<%@ attribute name="value" required="false" description="Expression to be evaluated." %>
<%@ attribute name="scope" required="false" description="Scope for var." %>
<%@ attribute name="condition" required="false" description="If this condition is not met, the value will not be set. If not provided, defaults to true" %>
<%@ attribute name="falseValue" required="false" description="If the condition is not met, this is what the value will be set to. If not supplied, no change is made" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" uri="http://3wks.com/thundr/tags" %>
<t:if condition="${empty condition or condition}">
	<c:set var="${var}" value="${value}" scope="${scope}"/>
</t:if>
<t:elseif condition="${not empty falseValue}">
	<c:set var="${var}" value="${falseValue}" scope="${scope}"/>
</t:elseif>