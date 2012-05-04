<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" description="The string to be url encoded." %>
<%@ tag import="java.net.URLEncoder" %>
<%= URLEncoder.encode(value, "UTF-8") %>