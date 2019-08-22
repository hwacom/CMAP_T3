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
    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/icon_new_taipei_city.ico">

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
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/logo_new_taipei_icon.png" width="auto" height="40" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/logo_new_taipei_word.png" width="auto" height="40" style="padding-top: 3px" />
			  			<span class="h3 font-weight-bold" style="color:#1C2269"><spring:message code="cmap.title" /></span>	
			  		</div>
			  	</div>
			  	<div class="row">
			  		<div class="col-md-6 col-sm-12 offset-md-3 m-t-5 login-form">
			  			<div class="row offset-md-1">
						  <div class="col-12">
						  	<img src="${pageContext.request.contextPath}/resources/images/openid_new_taipei.png" style="float: left; margin: 15px; witdh: 64px; height: 64px;"/>
							<p style="text-align: left;">
								<spring:message code="oidc.login.msg.1" /><br><spring:message code="oidc.login.msg.2" />
							</p>
			  			  </div>
			  			</div>
			  			<div class="row offset-md-1">
			  				<c:if test="${not empty LOGIN_EXCEPTION}">
			  					<div class="col-md-10 col-sm-12 center">
			  						<span class="red">
							        	<spring:message code="${LOGIN_EXCEPTION}" />
							      	</span>
			  					</div>
							</c:if>
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
            	<span class="font-weight-bold copyright" style="color: gray">Copyright &copy; <spring:message code="copyright" /></span>	
            </div>
        </div>
	</div>
  </body>
  
  <script>
	  $(document).ready(function() {
		  $("#btnLogin").click(function(e) {
			 location.href = "${pageContext.request.contextPath}/login/authByOIDC_NTPC";
		  });
	  });
  </script>
</html>
