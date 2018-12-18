<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>
  
   <style>
  	 body {
  		 overflow: hidden;
  	 }
   </style>

   <iframe id="dashboardFrame" width=100% height=450px frameborder="0"src="${IFRAME_URI }"></iframe>

<script src="${pageContext.request.contextPath}/resources/js/plugin/module/cmap.prtg.dashboard.js"></script>