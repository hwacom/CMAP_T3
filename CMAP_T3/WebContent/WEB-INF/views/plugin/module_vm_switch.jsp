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
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/module/cmap.module.vm.switch.min.js"></script>
	
  <body class="text-center" style="display: block; height: 100vh; background-color: #3e3e3e; background-image: repeating-linear-gradient(#3e3e3e 0px, #3e3e3e 10px, #202020 10px, #202020 12px); color: #fff;">
  	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	<div class="processing2" style="display: none;">
	  <div class="row col-12 center">
		<div id="msg_from_server" style="top: 35vh; position: relative; resize: none; margin-top: 30px; white-space: pre-line; word-wrap: break-word; color: #FAFAD2; background-color: #012456; padding: 5px; width: 50%; height: 250px; text-align: left;	overflow: auto;"></div>
	  </div>
	  <div class="row col-12 center">
	    <button type="button" id="btnClose" style="position: relative; top: 36vh; display: none;" class="btn btn-success">關閉</button>
	  </div>
	</div>
	
	<!-- Container fluid  -->
    <!-- ============================================================== -->
	<div id="content" class="container-fluid">
	  	<div class="row col-12 center">
	  		<span style="font-size: 3em; font-weight: bold;">切換備援 Host name :</span>
	 	</div>
		<div class="row col-12 center">
		  	<span style="font-size: 6em; font-weight: bold;">[</span>
		  	<span style="font-size: 6em; font-weight: bold; color: red;">${VM_NAME }</span>
		  	<span style="font-size: 6em; font-weight: bold;">]</span>
		</div>
		<div id="checkMsg" class="row col-12 center">
		  	<span style="font-size: 3em; font-weight: bold;">設備狀態: <span id="vmStatusMsg" style="vertical-align: top; color: yellow;">(按下CHECk進行檢查)</span></span>
		</div>
		<div id="checkBtn" class="row col-12 center" style="padding-top: 25px;">
		  	<button type="button" class="btn btn-success btn-lg vmswitch-btn" style="font-size: 5em;" id="btnCheck">CHECK</button>
		</div>
		<div id="goMsg" class="row col-12 center" style="display: none;">
		  	<span style="font-size: 3em; font-weight: bold;">切換過程<font style="color: #00cdff;"><u>無法暫停或中止!!</u></font></span>
		</div>
		<div id="goBtn" class="row col-12 center" style="padding-top: 25px; display: none;">
		  	<button type="button" class="btn btn-danger btn-lg vmswitch-btn" id="btnGo">GO</button>
		</div>
    </div>
    
  </body>
</html>