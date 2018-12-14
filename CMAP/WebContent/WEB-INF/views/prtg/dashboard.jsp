<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>
  
   <style>
  	 body {
  		 overflow: hidden;
  	 }
   </style>

   <iframe id="dashboardFrame" width=100% height=450px frameborder="0"
		src="https://163.19.163.169:1443/public/mapshow.htm?id=2956&mapid=2dce289c-01ec-40ef-aa94-27bb5c7f3f4f">
	</iframe>

<script src="${pageContext.request.contextPath}/resources/js/plugin/module/cmap.prtg.dashboard.js"></script>