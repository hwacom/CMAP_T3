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
			  			<!-- 亞太 -->
 						<!-- 
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/aptg_logo_icon.png" width="auto" height="30" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/aptg_logo_word.png" width="auto" height="23" style="padding-top: 3px" />
			  			-->
			  			
			  			<!-- 桃機 -->
			  			<img class="img" src="${pageContext.request.contextPath}/resources/images/logo_new_icon.png" width="auto" height="40" style="padding-top: 3px" />
      					<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/logo_new_word_short.png" width="auto" height="40" style="padding-top: 3px" />
			  			
			  			<span class="h3 font-weight-bold" style="color:#1C2269"><spring:message code="cmap.title" /></span>	
			  		</div>
			  	</div>
			  	<div class="row">
			  		<div class="col-md-6 col-sm-12 offset-md-3 m-t-5 login-form">
				  		<form class="form-signin" name="f" method='POST'>
						  <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
						      <span class="red">
						        	<spring:message code="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
						      </span>
						  </c:if>
				  		  <div class="form-group row">
				  		  	<div class="col-md-12 col-sm-12">
				  		  		<!-- <span class="h6 mb-3 font-weight-bold" style="color: #FFFF99">本系統採用教育局開放式認證服務</span> -->
				  		  	</div>
				  		  </div>
				  		  <div class="form-group row" style="vertical-align: middle;">
				  		  	<div class="col-3">
				  		  		<span class="h5 font-weight-bold" style="color: black"><spring:message code="login.account" />:</span>
				  		  	</div>
				  		  	<div class="col-9">
				  		  		<label for="inputAccount" class="sr-only"><spring:message code="login.account" /></label>
				  		  		<input type="text" name="username" id="inputAccount" class="form-control" placeholder="<spring:message code="login.account" />" required autofocus>
				  		  	</div>
				  		  </div>
					      <div class="form-group row">
					      	<div class="col-3">
					      		<span class="h5 font-weight-bold" style="color: black"><spring:message code="login.password" />:</span>
				  		  	</div>
				  		  	<div class="col-9">
				  		  		<label for="inputPassword" class="sr-only"><spring:message code="login.password" /></label>
				  		  		<input type="password" name="password" id="inputPassword" class="form-control" placeholder="<spring:message code="login.password" />" required>
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
					      <div class="row">
				  		  	<div class="col-sm-12">
				  		  		<button class="btn btn-block btn-success" type="submit"><spring:message code="login" /></button> <!-- Sign in -->
				  		  	</div>
				  		  </div>
					    </form>
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
</html>
