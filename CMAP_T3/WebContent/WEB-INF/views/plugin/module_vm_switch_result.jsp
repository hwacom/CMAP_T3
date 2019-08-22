<%@page import="java.util.Objects"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta name="description" content="">
    <meta name="author" content="">
	<meta name="ctx" content="${pageContext.request.contextPath}" />    
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/icon.ico">

    <title><spring:message code="cmap.title" /></title>
    
    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- JQuery-UI -->
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.structure.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.theme.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
	<link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
	
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui/jquery-ui.min.js"></script>
	
	<script src="${pageContext.request.contextPath}/resources/js/modernizr/modernizr-custom.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/common.min.js"></script>
	<!-- 
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/module/cmap.module.vm.switch.min.js"></script>
	 -->
	
  <body class="text-center" style="display: block; height: 100vh; background-color: #3e3e3e; background-image: repeating-linear-gradient(#3e3e3e 0px, #3e3e3e 10px, #202020 10px, #202020 12px); color: #fff;">
  	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	
    
  </body>
</html>