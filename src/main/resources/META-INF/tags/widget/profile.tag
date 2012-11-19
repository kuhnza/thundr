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
<c:if test="${not empty profileSession.events}">
<section id="${id}" class="thundr-profiler ${cssClass}">
	<style>
		.thundr-profiler { z-index: 10001; }
		.thundr-profiler { position: fixed; top: 0; right: 0; width: 800px; min-height: 200px; max-height: 800px; overflow-y: auto; padding: 0 12px; border: 4px solid #333; background: rgba(255, 255, 255, 0.85); -webkit-border-radius: 6px; -moz-border-radius: 6px; border-radius: 6px; }
		.thundr-profiler h1 { font-size: 18px; }
		.thundr-profiler table {width: 100%; }
		.thundr-profiler tr { height: 20px; background-color: transparent; }
		.thundr-profiler th { height: 20px; background-color: transparent; }
		.thundr-profiler td { height: 20px; max-height: 20px; border: 1px solid #CCC; margin: 0; padding: 0; background-color: transparent; position: relative; overflow: hidden; }
		.thundr-profiler th:first-child { width: 60px; }
		.thundr-profiler th:nth-child(2) { width: 200px;  }
		.thundr-profiler th:nth-child(3) { width: 70px;  }
		.thundr-profiler th:nth-child(4) { width: 48px; }
		.thundr-profiler th:last-child { width: 100%; }
		.thundr-profiler td:nth-child(3) { width: 70px; text-align: right; }
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
		.thundr-profiler .data { overflow: hidden; word-wrap: break-word; height: 20px; }
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
		.thundr-profiler .event.Search { background-color: rgb(200, 200, 220);}
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
			// v1.3 of colResizable, can be found here: http://quocity.com/colresizable/
			//colResizable - by Alvaro Prieto Lauroba - MIT & GPL 
			(function(a){function h(b){var c=a(this).data(q),d=m[c.t],e=d.g[c.i];e.ox=b.pageX;e.l=e[I]()[H];i[D](E+q,f)[D](F+q,g);P[z](x+"*{cursor:"+d.opt.dragCursor+K+J);e[B](d.opt.draggingClass);l=e;if(d.c[c.i].l)for(b=0;b<d.ln;b++)c=d.c[b],c.l=j,c.w=c[u]();return j}function g(b){i.unbind(E+q).unbind(F+q);a("head :last-child").remove();if(l){l[A](l.t.opt.draggingClass);var f=l.t,g=f.opt.onResize;l.x&&(e(f,l.i,1),d(f),g&&(b[G]=f[0],g(b)));f.p&&O&&c(f);l=k}}function f(a){if(l){var b=l.t,c=a.pageX-l.ox+l.l,f=b.opt.minWidth,g=l.i,h=1.5*b.cs+f+b.b,i=g==b.ln-1?b.w-h:b.g[g+1][I]()[H]-b.cs-f,f=g?b.g[g-1][I]()[H]+b.cs+f:h,c=s.max(f,s.min(i,c));l.x=c;l.css(H,c+p);if(b.opt.liveDrag&&(e(b,g),d(b),c=b.opt.onDrag))a[G]=b[0],c(a)}return j}function e(a,b,c){var d=l.x-l.l,e=a.c[b],f=a.c[b+1],g=e.w+d,d=f.w-d;e[u](g+p);f[u](d+p);a.cg.eq(b)[u](g+p);a.cg.eq(b+1)[u](d+p);if(c)e.w=g,f.w=d}function d(a){a.gc[u](a.w);for(var b=0;b<a.ln;b++){var c=a.c[b];a.g[b].css({left:c.offset().left-a.offset()[H]+c.outerWidth()+a.cs/2+p,height:a.opt.headerOnly?a.c[0].outerHeight():a.outerHeight()})}}function c(a,b){var c,d=0,e=0,f=[];if(b)if(a.cg[C](u),a.opt.flush)O[a.id]="";else{for(c=O[a.id].split(";");e<a.ln;e++)f[y](100*c[e]/c[a.ln]+"%"),b.eq(e).css(u,f[e]);for(e=0;e<a.ln;e++)a.cg.eq(e).css(u,f[e])}else{O[a.id]="";for(e in a.c)c=a.c[e][u](),O[a.id]+=c+";",d+=c;O[a.id]+=d}}function b(b){var e=">thead>tr>",f='"></div>',g=">tbody>tr:first>",i=">tr:first>",j="td",k="th",l=b.find(e+k+","+e+j);l.length||(l=b.find(g+k+","+i+k+","+g+j+","+i+j));b.cg=b.find("col");b.ln=l.length;b.p&&O&&O[b.id]&&c(b,l);l.each(function(c){var d=a(this),e=a(b.gc[z](w+"CRG"+f)[0].lastChild);e.t=b;e.i=c;e.c=d;d.w=d[u]();b.g[y](e);b.c[y](d);d[u](d.w)[C](u);if(c<b.ln-1)e.mousedown(h)[z](b.opt.gripInnerHtml)[z](w+q+'" style="cursor:'+b.opt.hoverCursor+f);else e[B]("CRL")[A]("CRG");e.data(q,{i:c,t:b[v](o)})});b.cg[C](u);d(b);b.find("td, th").not(l).not(N+"th, table td").each(function(){a(this)[C](u)})}var i=a(document),j=!1,k=null,l=k,m=[],n=0,o="id",p="px",q="CRZ",r=parseInt,s=Math,t=a.browser.msie,u="width",v="attr",w='<div class="',x="<style type='text/css'>",y="push",z="append",A="removeClass",B="addClass",C="removeAttr",D="bind",E="mousemove.",F="mouseup.",G="currentTarget",H="left",I="position",J="}</style>",K="!important;",L=":0px"+K,M="resize",N="table",O,P=a("head")[z](x+".CRZ{table-layout:fixed;}.CRZ td,.CRZ th{padding-"+H+L+"padding-right"+L+"overflow:hidden}.CRC{height:0px;"+I+":relative;}.CRG{margin-left:-5px;"+I+":absolute;z-index:5;}.CRG .CRZ{"+I+":absolute;background-color:red;filter:alpha(opacity=1);opacity:0;width:10px;height:100%;top:0px}.CRL{"+I+":absolute;width:1px}.CRD{ border-left:1px dotted black"+J);try{O=sessionStorage}catch(Q){}a(window)[D](M+"."+q,function(){for(a in m){var a=m[a],b,c=0;a[A](q);if(a.w!=a[u]()){a.w=a[u]();for(b=0;b<a.ln;b++)c+=a.c[b].w;for(b=0;b<a.ln;b++)a.c[b].css(u,s.round(1e3*a.c[b].w/c)/10+"%").l=1}d(a[B](q))}});a.fn.extend({colResizable:function(c){c=a.extend({draggingClass:"CRD",gripInnerHtml:"",liveDrag:j,minWidth:15,headerOnly:j,hoverCursor:"e-"+M,dragCursor:"e-"+M,postbackSafe:j,flush:j,marginLeft:k,marginRight:k,disable:j,onDrag:k,onResize:k},c);return this.each(function(){var d=c,e=a(this);if(d.disable){if(e=e[v](o),(d=m[e])&&d.is(N))d[A](q).gc.remove(),delete m[e]}else{var f=e.id=e[v](o)||q+n++;e.p=d.postbackSafe;if(e.is(N)&&!m[f])e[B](q)[v](o,f).before(w+'CRC"/>'),e.opt=d,e.g=[],e.c=[],e.w=e[u](),e.gc=e.prev(),d.marginLeft&&e.gc.css("marginLeft",d.marginLeft),d.marginRight&&e.gc.css("marginRight",d.marginRight),e.cs=r(t?this.cellSpacing||this.currentStyle.borderSpacing:e.css("border-spacing"))||2,e.b=r(t?this.border||this.currentStyle.borderLeftWidth:e.css("border-"+H+"-"+u))||1,m[f]=e,b(e)}})}})})(jQuery)
			thundrProfiler = {
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
				show: function(){
					$(".thundr-profiler").css('right', 0).hide().fadeIn();
				},
				close: function(){
					$(".thundr-profiler").first().fadeOut();
				}
			};
			$(document).ready(function(){
				$(".thundr-profiler .event").mouseenter(thundrProfiler.tooltip.show).mouseleave(thundrProfiler.tooltip.hide);
				$(".thundr-profiler .close").click(thundrProfiler.close);
				$(".thundr-profiler table").colResizable({postbackSafe : true});
			});
		}
	</script>
	<c:set var="now" value="${profileSession.now}"/>
	<c:set var="duration" value="${now - profileSession.start}"/>
	<header><h1>${profileSession.data} - ${duration}ms <span class="close" title="Close Profile View">X</span></h1></header>
	<table class="thundr-profiler-table">
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
				<td><div class="data">${event.category}</div></td>
				<td><div class="data">${event.data}</div></td>
				<td>${eventDuration}ms</td>
				<td class="status ${event.status}">&nbsp;</td>
				<td><div class="event ${event.category}" style="top: 0; left: ${left}%; width: ${width}%;" data-duration="${eventDuration}" data-status="${event.status}" data-category="${event.category}"  data-data="${event.data}">&nbsp;</div></td>
			</tr>
		</c:forEach>
			<tr>
				<td><div class="data">Overall Request</div></td>
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