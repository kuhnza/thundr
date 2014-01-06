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
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" description=""%>
<%@ attribute name="var" required="false" description="Name of the exported variable to hold the value specified in the action. The type of the variable is whatever type the value expression evaluates to." %>
<%@ attribute name="value" type="java.lang.Object" description="Expression to be evaluated." %>
<%@ attribute name="condition" required="false" description="If this condition is not met, the value will not be set. If not provided, defaults to true" %>
<%@ attribute name="falseValue" type="java.lang.Object"  required="false" description="If the condition is not met, this is what the value will be set to. If not supplied, no change is made" %>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags" %>
<%
	boolean isTrue = condition == null || condition.length() == 0 || Boolean.valueOf(condition);
	boolean hasVar = var != null && var.length() > 0;
	if(isTrue){
		if(hasVar){
			request.setAttribute(var, value);
		}else{
			out.print(value);	
		}
	} else if (falseValue != null){
		if(hasVar){
			request.setAttribute(var, falseValue);
		}else{
			out.print(falseValue);	
		}
	}
%>