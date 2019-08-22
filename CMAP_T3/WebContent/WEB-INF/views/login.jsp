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
    
   	<!-- Favicon icon -->
    <!-- 桃機 -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/icon.ico">
    
    <!-- 台鐵 -->
    <!-- 
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/taiwan_railway.ico">
	 -->
	 
    <title><spring:message code="cmap.title" /></title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <!-- 桃機 -->
	<link href="${pageContext.request.contextPath}/resources/css/signin_T3.css" rel="stylesheet">
	
	<!-- 台鐵 -->
	<!-- 
	<link href="${pageContext.request.contextPath}/resources/css/signin_TRA.css" rel="stylesheet">
	-->
	
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	
  <body class="text-center">
  	<div class="mask"></div>
  
  	<!-- Container fluid  -->
    <!-- ============================================================== -->
    <div class="container">
    	<!-- ============================================================== -->
        <!-- Start Page Content -->
        <!-- ============================================================== -->
        <div class="row" style="height: 20vh; align-content: center; margin-left: -10%;">
        	<!-- 桃機 -->
        	<img class="img" src="${pageContext.request.contextPath}/resources/images/logo_new_icon.png" width="auto" height="80" style="padding-top: 3px; z-index: 1004;" />
      		<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/logo_new_word_short_white.png" width="auto" height="30" style="margin-top: 50px; z-index: 1004;" />
        	
        	<!-- 台鐵 -->
        	<!--
	      	<img class="img" src="${pageContext.request.contextPath}/resources/images/taiwan_railway_icon.png" width="auto" height="80" style="padding-top: 3px; z-index: 1004;" />
	  		<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/taiwan_railway_word.png" width="auto" height="50" style="margin-top: 33px; z-index: 1004; margin-left: 5px;" />
        	-->
        </div>
        <div class="row" style="height: 55vh;">
            <div class="col-12">
	  			<div class="row center">
			  		<div class="col-4 login-title">
			  			<!-- 亞太 -->
			  			<!-- 
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/aptg_logo_icon.png" width="auto" height="30" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/aptg_logo_word.png" width="auto" height="23" style="padding-top: 3px" />
			  			-->
			  			
			  			<!-- 桃機 -->
			  			<!-- 
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/logo_new_icon.png" width="auto" height="40" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/logo_new_word_short.png" width="auto" height="40" style="padding-top: 3px" />
			  			-->
			  			
			  			<span class="h3"><spring:message code="cmap.title" /></span>	
			  		</div>
			  	</div>
			  	<div class="row center">
			  		<div class="col-3 login-form">
				  		<form class="form-signin" name="f" method='POST'>
						  <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
						      <span class="red">
						        	<spring:message code="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
						      </span>
						  </c:if>
				  		  <div class="form-group row" style="margin-right: 1%;">
				  		    <div class="col-1"></div>
				  		  	<div class="col-10 left" style="padding-left: 0px; margin-bottom: 10px;">
				  		  		<span class="h5" style="font-weight: bold;">使用者登入</span>
				  		  	</div>
				  		  </div>
				  		  <div class="form-group row offset-1" style="vertical-align: middle; border-bottom: solid 2px #faa21b; padding-bottom: 5px; margin-right: 1%;">
				  		  	<div class="col-3" style="padding-left: 0px;">
				  		  		<span class="h6" style="color: black; font-weight: bold;"><spring:message code="login.account" /></span>
				  		  	</div>
				  		  	<div class="col-9">
				  		  		<label for="inputAccount" class="sr-only"><spring:message code="login.account" /></label>
				  		  		<input type="text" name="username" id="inputAccount" required autofocus>
				  		  	</div>
				  		  </div>
					      <div class="form-group row offset-1" style="vertical-align: middle; border-bottom: solid 2px #faa21b; padding-bottom: 5px; margin-right: 1%;">
					      	<div class="col-3" style="padding-left: 0px;">
					      		<span class="h6" style="color: black; font-weight: bold;"><spring:message code="login.password" /></span>
				  		  	</div>
				  		  	<div class="col-9">
				  		  		<label for="inputPassword" class="sr-only"><spring:message code="login.password" /></label>
				  		  		<input type="password" name="password" id="inputPassword" required>
				  		  	</div>
				  		  </div>
				  		  <!-- 
					      <div class="row">
				  		  	<div class="col-md-12 col-sm-12">
				  		  		<div class="checkbox mb-3">
							        <label>
							          <input type="checkbox" value="remember-me"> <spring:message code="login.rememberMe"/>
							        </label>
							    </div>
				  		  	</div>
				  		  </div>
				  		   -->
					      <div class="row center" style="margin-top: 70px;">
				  		  	<div class="col-7">
				  		  		<button class="btn btn-block btn-success" type="submit"><spring:message code="login" /></button> <!-- Sign in -->
				  		  	</div>
				  		  </div>
					    </form>
					</div>
			  	</div>
	  		</div>
		</div>
		<div class="row" style="height: 25vh; align-content: end; padding-bottom: 20px;">
            <div class="col-12">
            	<span style="color: #fff; font-size: .7rem; font-weight: bold;">&copy; HwaCom Systems Inc. All Rights Reserved.</span>	
            </div>
        </div>
	</div>
  </body>
</html>
