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
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/test.min.js"></script>
  </head>
  
  <body class="text-center" style="display: block; height: 100vh; background-color: #3e3e3e; background-image: repeating-linear-gradient(#3e3e3e 0px, #3e3e3e 10px, #202020 10px, #202020 12px); color: #fff;">
  	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	
  	<!-- Container fluid  -->
    <!-- ============================================================== -->
  	<div id="content" class="container-fluid">
		<!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
		<div class="row">
		  <!-- [START]查詢欄位bar -->
	      <div class="col-12">
	      	<form>
	      		<div class="container-fluid">
		      	  <div class="form-group row">
		      	  	<div class="col-3 group-field-other">
		    	    	<span class="font-weight-bold" style="width: 20%">Source_ID</span>
		    	    	<input type="text" id="input_SourceId" style="width: 75%" />
		    	    </div>
		    	    <div class="col-4 group-field-other">
		    	    	<span class="font-weight-bold" style="width: 20%">Role</span>
		    	    	<input type="text" id="input_Role" style="width: 75%" />
		    	    </div>
		    	    <div class="col-3 group-field-other">
		    	    	<span class="font-weight-bold" style="width: 20%">Account</span>
		    	    	<input type="text" id="input_Account" style="width: 75%" />
		    	    </div>
					<div class="col-2" style="padding-top: 5px;">
		    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btn_getPasshash">getPasshash</button>
		    	    </div>
		      	  </div>
		      	</div>
			</form>
	      </div>
	      <!-- [END]查詢欄位bar -->
	    </div>
	    
	    <!-- 查詢結果TABLE區塊 -->
		<div class="row center">
		  <pre id="div_Result" style="width: 90%; height: 70vh; background-color: #fff; color: black; text-align: left; padding: 0.5rem 1rem; font-size: 1rem;">
		  
		  </pre>
		</div>
	</div>
  
  </body>
</html>