<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp" %>
<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/icon.ico">

    <title><spring:message code="cmap.title" /></title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
	<link href="${pageContext.request.contextPath}/resources/css/signin.css" rel="stylesheet">
	
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	
  <body class="text-center">
  	<!-- Container fluid  -->
    <!-- ============================================================== -->
    <div class="container" style="margin-bottom: 50px">
    	<!-- ============================================================== -->
        <!-- Start Page Content -->
        <!-- ============================================================== -->
        <div class="row">
            <div class="col-12">
	  			<div class="row">
			  		<div class="col-md-6 col-sm-12 offset-md-3 m-t-5 login-title">
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/Logo_icon.png" width="auto" height="40" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/Logo_word.png" width="auto" height="40" style="padding-top: 3px" />
			  			<span class="h3 font-weight-bold" style="color:#1C2269"><spring:message code="cmap.title" /></span>	
			  		</div>
			  	</div>
			  	<div class="row">
			  		<div class="col-md-6 col-sm-12 offset-md-3 m-t-5 login-form">
			  			<c:if test="${not empty LOGIN_EXCEPTION}">
					      <span class="red">
					        	<spring:message code="${LOGIN_EXCEPTION}" />
					      </span>
						</c:if>
			  			<div class="row offset-md-1">
						  <div class="col-12">
						  	<img src="${pageContext.request.contextPath}/resources/images/mlc_sso.png" style="float: left; margin: 15px;"/>
							<p style="text-align: left;">本系統採用苗栗縣教育雲端帳號認證服務<br>按下登入後將導向該登入頁面</p>
			  			  </div>
			  			</div>
			  			<div class="row offset-md-1">
			  		  		<div class="col-md-10 col-sm-12">
			  		  			<button class="btn btn-block btn-success" type="submit" id="btnLogin"><spring:message code="login" /></button> <!-- Sign in -->
			  		  		</div>
			  		    </div>
					</div>
			  	</div>
	  		</div>
		</div>
		<div class="row">
            <div class="col-12">
            	<span class="font-weight-bold copyright" style="color: gray">Copyright &copy; 2018-2019 HwaCom Systems Inc. All Rights Reserved.</span>	
            </div>
        </div>
	</div>
  </body>
  
  <script>
	  $(document).ready(function() {
		  $("#btnLogin").click(function(e) {
			 location.href = "${pageContext.request.contextPath}/login/authByOIDC";
		  });
	  });
  </script>
</html>
