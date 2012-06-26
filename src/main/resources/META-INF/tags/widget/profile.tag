<%@ tag import="com.threewks.thundr.profiler.ProfileSession" %>
<%@ tag import="com.threewks.thundr.profiler.Profiler" %>
<%@ tag body-content="empty" dynamic-attributes="dynAttrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="id" %>
<%@ attribute name="cssClass" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% 
Profiler profiler = (Profiler) getJspContext().getAttribute("com.threewks.thundr.profiler.Profiler", PageContext.REQUEST_SCOPE);
ProfileSession profileSession = profiler == null ? null : profiler.getCurrent();
getJspContext().setAttribute("profileSession", profileSession, PageContext.PAGE_SCOPE);
%>
<c:if test="${not empty profileSession}">
<section id="${id}" class="thundr-profiler ${cssClass}">
	<style>
		.thundr-profiler { z-index: 10001; }
		.thundr-profiler { position: fixed; top: 0; right: 0; width: 800px; min-height: 200px; padding: 0 12px; border: 4px solid #333; background: rgba(255, 255, 255, 0.85); -webkit-border-radius: 6px; -moz-border-radius: 6px; border-radius: 6px; }
		.thundr-profiler h1 { font-size: 18px; }
		.thundr-profiler tr { height: 20px; background-color: transparent; }
		.thundr-profiler th { height: 20px; background-color: transparent; }
		.thundr-profiler td { width: 120px; height: 20px; border: 1px solid #CCC; margin: 0; padding: 0; background-color: transparent; position: relative; white-space: nowrap; overflow: hidden; }
		.thundr-profiler td:nth-child(3) { width: 20px; height: 20px; background-color: transparent; position: relative; }
		.thundr-profiler td:last-child { width: 100%; }
		.thundr-profiler thead tr { height: 20px; border: 1px solid #333; text-align: left;
			background: #666666;
			background: url(data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/Pgo8c3ZnIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgdmlld0JveD0iMCAwIDEgMSIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+CiAgPGxpbmVhckdyYWRpZW50IGlkPSJncmFkLXVjZ2ctZ2VuZXJhdGVkIiBncmFkaWVudFVuaXRzPSJ1c2VyU3BhY2VPblVzZSIgeDE9IjAlIiB5MT0iMCUiIHgyPSIwJSIgeTI9IjEwMCUiPgogICAgPHN0b3Agb2Zmc2V0PSIwJSIgc3RvcC1jb2xvcj0iI2NjY2NjYyIgc3RvcC1vcGFjaXR5PSIxIi8+CiAgICA8c3RvcCBvZmZzZXQ9IjEwMCUiIHN0b3AtY29sb3I9IiM2NjY2NjYiIHN0b3Atb3BhY2l0eT0iMSIvPgogIDwvbGluZWFyR3JhZGllbnQ+CiAgPHJlY3QgeD0iMCIgeT0iMCIgd2lkdGg9IjEiIGhlaWdodD0iMSIgZmlsbD0idXJsKCNncmFkLXVjZ2ctZ2VuZXJhdGVkKSIgLz4KPC9zdmc+);
			background: -moz-linear-gradient(top,  #cccccc 0%, #666666 100%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#cccccc), color-stop(100%,#666666)); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top,  #cccccc 0%,#666666 100%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top,  #cccccc 0%,#666666 100%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top,  #cccccc 0%,#666666 100%); /* IE10+ */
			background: linear-gradient(top,  #cccccc 0%,#666666 100%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#cccccc', endColorstr='#666666',GradientType=0 ); /* IE6-8 */
		}
		.thundr-profiler tbody { border: 1px solid #333; }
		.thundr-profiler .data { width: 120px; overflow: hidden; }
		.thundr-profiler .status { width: 16px; padding: 0 3px;}
		.thundr-profiler .status.Success { background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAXklEQVQYV2NkIBIwElLnsMDhP0gNXoVgRQZAVReQFIIEDyQcgGuEKwIZB1OIrBOkGEURisILEHeAAVA32DokPkgz2CoME9AUoXgGm3XIbkbxNbpbkYMOI3jQfQ9TDACrtC2Hg3dj3AAAAABJRU5ErkJggg==) center center no-repeat;}
		.thundr-profiler .status.Failed { background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAcElEQVQYV22Q0RHAIAhDy0aO4CgdraM4ghu1hCNepPrjCc8kYLP39/LTxjDc9aA/vWgETzB7AQJ4/FdLKSordLvbslOY9lAChPeWS2GFfqDmRfOoqJlisPQnHNY1+MqYq9vWUzMpHCAGQJHT1YWz/wGli0KmQ0Y8kAAAAABJRU5ErkJggg==) center center no-repeat;}
		.thundr-profiler .status.Timeout { background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAKCAYAAABmBXS+AAAAXElEQVQYV2NkQAJfv0b8B3Ev7TnFYOl/jxEmBWeABIhWFBR0iGHdOjsU0+AmwUzBqQimAGQlTBGy28AmEVSErADZpzA2yKeM6IqQrYNZyXh8oxI4bPABlHDCpRAAZupG1lD4iXgAAAAASUVORK5CYII=) center center no-repeat;}
		.thundr-profiler .status.Unknown { background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAF0lEQVQYV2NkIBIwEqmOYVQh3pAiOngACmkAC8i6MuwAAAAASUVORK5CYII=) center center no-repeat;}

		.thundr-profiler .event { position: absolute; height: 12px; margin: 4px 0 0 0; border: 1px solid #333; background: #DDD; }
		.thundr-profiler .event.requestDuration { left: 1px; right: 1px; top: 0; }		
		.thundr-profiler .event.Action { background-color: rgb(255, 255, 200);}
		.thundr-profiler .event.Database { background-color: rgb(255, 220, 220);}
		.thundr-profiler .event.DatabaseRead { background-color: rgb(255, 240, 230);}
		.thundr-profiler .event.DatabaseWrite { background-color: rgb(255, 200, 200);}
		.thundr-profiler .event.Http { background-color: rgb(220, 220, 255);}
		.thundr-profiler .event.HttpRequest { background-color: rgb(200, 200, 255);}
		.thundr-profiler .event.HttpResponse { background-color: rgb(235, 235, 255);}
		.thundr-profiler .event.Service { background-color: rgb(220, 255, 220);}
		.thundr-profiler .event.ServiceRequest { background-color: rgb(200, 255, 200);}
		.thundr-profiler .event.ServiceResponse { background-color: rgb(235, 255, 235);}
		.thundr-profiler .event.BusinessLogic { background-color: rgb(200, 255, 200);}
		.thundr-profiler .event.View { background-color: rgb(255, 200, 255);}
		
		.thundr-profiler .thundr-profiler-tooltip { position: fixed; display: none; top: 0; left: 0; min-width: 300px; padding: 12px 20px; border: 4px solid #333; background: rgba(255, 255, 255, 0.85); -webkit-border-radius: 6px; -moz-border-radius: 6px; border-radius: 6px; }
		.thundr-profiler .thundr-profiler-tooltip .tooltip-title { display: inline-block; width: 60px;}
		
		.thundr-profiler .close { float: right; margin: 4px 8px 0 0; padding: 4px; border: 2px solid #000; line-height: 14px; font-size: 14px;}
	</style>
	<script>
		if($){
			var thundrProfiler = {
				tooltip: {
					show :function(e){
						var el = $(this);
						var tooltipEl = $(".thundr-profiler-tooltip");
						 $.each(el[0].attributes, function(index, attr){
							 var attrName = attr.name;
							 if(attrName.indexOf("data-") == 0){
								 tooltipEl.find("."+attrName.substring(5)).html(attr.value);
							 }
						  });
						 tooltipEl.show();
					},
					hide :function(e){
						$(".thundr-profiler-tooltip").hide();
					}
				},
				close: function(){
					$(this).parents(".thundr-profiler").first().hide();
				}
			};
			$(document).ready(function(){
				$(".thundr-profiler .event").mouseenter(thundrProfiler.tooltip.show).mouseleave(thundrProfiler.tooltip.hide);
				$(".thundr-profiler .close").click(thundrProfiler.close);
				
			});	
		}
	</script>
	<c:set var="now" value="${profileSession.now}"/>
	<c:set var="duration" value="${now - profileSession.start}"/>
	<header><h1>${profileSession.data} - ${duration}ms <span class="close" title="Close Profile View">X</span></h1></header>
	<table>
		<thead>
			<tr>
			<th>Category</th>
			<th>Event</th>
			<th>Duration</th>
			<th>Status</th>
			<th>Timeline</th>
			</tr>
		</thead>
		<tbody>
		
		<c:forEach items="${profileSession.events}" var="event">
			<c:set var="eventStart" value="${event.start}"/>
			<c:set var="eventEnd" value="${now}"/>
			<c:if test="${event.end > 0}">
				<c:set var="eventEnd" value="${event.end}"/>
			</c:if>
			<c:set var="eventDuration" value="${eventEnd - eventStart}"/>
			<c:set var="left" value="${(eventStart - profileSession.start)/duration*100}"/>
			<c:set var="width" value="${eventDuration/duration*100}"/>
			<tr>
				<td>${event.category}</td>
				<td><div class="data">${event.data}</div></td>
				<td>${eventDuration}ms</td>
				<td class="status ${event.status}">&nbsp;</td>
				<td><div class="event ${event.category}" style="top: 0; left: ${left}%; width: ${width}%;" data-duration="${eventDuration}" data-status="${event.status}" data-category="${event.category}"  data-data="${event.data}">&nbsp;</div></td>
			</tr>
		</c:forEach>
			<tr>
				<td>Overall Request</td>
				<td>&nbsp;</td>
				<td>${duration}ms</td>
				<td>&nbsp;</td>
				<td><div class="event requestDuration" data-duration="${duration}" data-status="Complete" data-category="Request"  data-data="${profileSession.data}">&nbsp;</div></td>
			</tr>
		</tbody>
	</table>
	<div class="thundr-profiler-tooltip">
		<div><span class="tooltip-title">Category:</span><span class="category"></span></div>
		<div><span class="tooltip-title">Data:</span><span class="data"></span></div>
		<div><span class="tooltip-title">Status:</span> <span class="status"></span></div>
		<div><span class="tooltip-title">Duration:</span><span class="duration"></span>ms</div>
	</div>
</section>
</c:if>