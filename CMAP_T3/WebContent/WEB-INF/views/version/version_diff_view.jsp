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
	<meta name="timeout" content="${timeout}" />
	<meta name="diff" content="${diffPos }" /> 
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
	<!-- Icons -->
    <script src="${pageContext.request.contextPath}/resources/js/feather-icons/feather.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/modernizr/modernizr-custom.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/common.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.version.diff.view.min.js"></script>
  </head>
  
  <body class="text-center" style="display: block; height: 100vh; background-color: #3e3e3e; background-image: repeating-linear-gradient(#3e3e3e 0px, #3e3e3e 10px, #202020 10px, #202020 12px); color: #fff;">
  	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	
	<!-- Container fluid  -->
    <!-- ============================================================== -->
	<div id="content" class="container-fluid">
	  	<div class="form-group row">
	    	<div class="col-1"></div>
	    	<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label"><spring:message code="config.version.short" /> :</label>
			<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionLeft" value="${versionLeft }" readonly>
			<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label"><spring:message code="config.version.short" /> :</label>
			<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionRight" value="${versionRight }" readonly>
	    </div>
	    <div class="form-group row">
	    	<div class="form-control form-control-sm col-1 diff-compare-line script" id="compareModal_contentLineNum">${versionLineNum }</div>
	    	<div class="form-control form-control-sm col-5 diff-compare-content nowrap script" id="compareModal_contentLeft">${contentLeft }</div>
	    	<div class="form-control form-control-sm col-5 diff-compare-content nowrap script" id="compareModal_contentRight">${contentRight }</div>
	    	<div class="col-1">
	    		<span data-feather="chevrons-up" class="feather-compare" id="jumpToTop"></span>
	    		<span data-feather="chevron-up" class="feather-compare" id="jumpToPre"></span>
	    		<span data-feather="chevron-down" class="feather-compare" id="jumpToNext"></span>
	    		<span data-feather="chevrons-down" class="feather-compare" id="jumpToBottom"></span>
	    	</div>
	    	<div class="col-12 center">
	    		<span id="compareModal_summary"></span>
	    	</div>
	    </div>
	</div>
    
    <script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.main.min.js"></script>
  </body>
</html>