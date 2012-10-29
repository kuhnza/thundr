<%@tag import="java.util.Collections"%>
<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@tag import="java.util.Arrays"%>
<%@tag import="java.util.Collection"%>
<%@tag import="java.util.Map"%>
<%@tag import="java.util.LinkedHashMap"%>
<%@ tag dynamic-attributes="dynAttrs" trimDirectiveWhitespaces="true"%>
<%@ attribute name="name" required="true"%>
<%@ attribute name="value" type="java.lang.Object" %>
<%@ attribute name="items" type="java.lang.Object" required="true"%>
<%@ attribute name="labels" type="java.lang.Object" %>
<%@ attribute name="multiple" type="java.lang.Boolean" %>
<%@ attribute name="id"%>
<%@ attribute name="cssClass"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags" %>
<%	
	Map<Object, Object> itemsMap = new LinkedHashMap<Object, Object>();
	
	ArrayList<Object> valueList = new ArrayList<Object>();
	if (value.getClass().isArray()) {
		valueList.addAll(Arrays.asList((Object[]) value));
	} else if (value instanceof Collection) {
		valueList.addAll((Collection)value);
	} else {
		valueList.add(value);
	}
	
		
	if (items instanceof Map) {
		itemsMap.putAll((Map)items);
	} else {

		List<Object> valueCollection = new ArrayList<Object>();
		List<Object> labelCollection = new ArrayList<Object>();
		
		if (items.getClass().isArray()) {
			valueCollection.addAll(Arrays.asList((Object[]) items));
		} else if (items instanceof Collection) {
			valueCollection.addAll((Collection)items);
		}
						
		if(labels == null){
			labelCollection.addAll(valueCollection);
		}else{
			if (labels.getClass().isArray()){
				labelCollection.addAll(Arrays.asList((Object[])labels));
			} else{
				labelCollection.addAll((Collection)labels);	
			}
		}
				
		for(int i = 0; i < valueCollection.size(); i++){						
			itemsMap.put(labelCollection.get(i), valueCollection.get(i));		
		}
	}
	
	getJspContext().setAttribute("items", itemsMap);
	getJspContext().setAttribute("values", valueList);
%>

<c:if test="${not empty id}">
	<c:set var="id" value="id='${id}'" />
</c:if>
<c:if test="${not empty cssClass}">
	<c:set var="cssClass" value="class='${cssClass}'" />
</c:if>
<select ${id} ${cssClass} name="${name}" <c:forEach items="${dynAttrs}" var="dynAttr">${dynAttr.key}="${dynAttr.value}"</c:forEach> ${multiple ? 'multiple="multiple"' : '' } />
	<jsp:doBody/>
<c:forEach items="${items}" var="item">
	<c:set var="selected" value="${false}"/>
	<c:if test="${t:contains(values, item.value)}">
		<c:set var="selected" value="${true}"/>
	</c:if>	
	<option value="${item.value}" ${selected ? 'selected="selected"' : ''}>${item.key}</option>
</c:forEach>
</select>